#!/usr/bin/env python2
# coding=utf-8
import sys

PYTHON = sys.version_info[0]

if PYTHON == 3:
    from socketserver import ThreadingMixIn
    from xmlrpc.server import SimpleXMLRPCServer
else:
    from SocketServer import ThreadingMixIn
    from SimpleXMLRPCServer import SimpleXMLRPCServer

import os
import struct
import time
import traceback

import modbus_tk.defines as cts
from modbus_tk import modbus_tcp
from pluginLog import Log
from dashboardService import DashboardService

logger = Log("LiftingColumn")
class ModbusTCP:

    DEFAULT_RETRY_DELAY_TIME = 0.01
    DEFAULT_RETRY_TIME = 5


    def __init__(self, ip, port=502, slaveID=1):
        self.ip = ip
        self.port = port
        self.slaveID = slaveID

        self.master = modbus_tcp.TcpMaster(host=self.ip, port=self.port,timeout_in_sec=0.5)


    def readHoldingRegisters(self, address, count=1):
        reTryNum = 0
        while reTryNum < self.DEFAULT_RETRY_TIME:
            try:
                res = self.master.execute(self.slaveID, function_code = cts.READ_HOLDING_REGISTERS, starting_address = address, quantity_of_x = count)

                if type(res) == bool:
                    reTryNum = reTryNum + 1    
                else:
                    return res
            except Exception as e:
                traceback.print_exc()
                logger.info("Read: Register->%s len->%s except:%s"%(str(address), str(count), e))
                reTryNum = reTryNum + 1    
                time.sleep(self.DEFAULT_RETRY_DELAY_TIME)
                # return False
        logger.info("Read: Register->%s len->%s ReTry Error"%(str(address), str(count)))
        return None


    def writeHoldingRegisters(self, address, values):
        reTryNum = 0
        while reTryNum < self.DEFAULT_RETRY_TIME:
            try:
                res = self.master.execute(self.slaveID, cts.WRITE_MULTIPLE_REGISTERS,  starting_address = address,output_value = values)
                if res == False:
                    reTryNum = reTryNum + 1    
                else:
                    return res
            except Exception as e:
                traceback.print_exc()
                logger.info("Write: Register->%s Values->%s except:%s"%(str(address), str(values), e))
                reTryNum = reTryNum + 1
                time.sleep(self.DEFAULT_RETRY_DELAY_TIME)
                # return False
        logger.info("Write: Register->%s Values->%s ReTry Error"%(str(address), str(values)))
        return None



class ZTLifting:

    DEFAULT_LENGTH = 500        # 默认行程
    REDUCTION_RATIO = 9         # 减速比
    SCREW_LEAD = 40             # 丝杆导程 mm
    SINGLE_TURN_PULSE = 10000   # 单圈脉冲
    PLUSE_ONE_MM = int(SINGLE_TURN_PULSE * REDUCTION_RATIO / SCREW_LEAD)     # 1mm对应的脉冲

    SPEED_CONVERTOR = round(((REDUCTION_RATIO * 60) / SCREW_LEAD), 3)        # mm/s * SPEED_CONVERTOR = RPM  60为60s
    DEFAULT_MAX_SPEED = 200


    class InovanceServo:
        """汇川伺服寄存器地址
        """
        # READ
        ABS_POSITION    = 0x0B00 + 7        # 0x0B07
        ACTUAL_SPEED    = 0x0B00 + 0        # 0x0B00
        TEMPERATURE     = 0x0B00 + 27       # 0x0B27
        ERROR_CODE      = 0x0B00 + 45       # 0x0B45
        CURRENT         = 0x0B00 + 24       # 0x0B24
        E_STOP          = 0x0D00 + 5        # 0x0D05
        FAULT_RESET     = 0x0D00 + 1        # 0x0D01

        # WRITE
        TARGET_POSITION = 0x1100 + 12       # 0x1112
        TARGET_SPEED    = 0x1100 + 14       # 0x1114
        VISUAL_DI       = 0x3100 + 00       # 0x3100

        # 此处应该是按位进行操作的
        NO_ACTION       = 0                 # None
        SERVO_ON        = 1                 # 上伺服
        JOG_UP          = 3                 # 点动上升
        JOG_DOWN        = 5                 # 点动下降
        MULTI_SPEED_POS = 9                 # 设置为多段速

    def __init__(self):

        self.connectStatus = False
        self.liftingLength = self.DEFAULT_LENGTH
        self.minLimit = None
        self.maxLimit = None

        self.moveBlockStatus = False        # 多线程中用来退出 move block状态
        self.dashboard = DashboardService()
        self.dashboard.add_listener(self.stop)

    @logger.logit(just_for_xmlrpc=True)
    def connect(self, ip, port, slaveID):
        self.ip = ip
        self.port = port
        self.slaveID = slaveID
        self.master = ModbusTCP(ip=self.ip, port=self.port, slaveID=self.slaveID)
        self.dashboard.start_listen()
    # todo 做连接状态的判断
        if self.testReadRegister() != None:
            logger.info("Connect Success")
            self.connectStatus = True   
        else:
            logger.info("Connect Failed")
            self.connectStatus = False
        return self.connectStatus


    @logger.logit(just_for_xmlrpc=True)
    def disConnect(self):
        self.connectStatus = False
        return True
    

    @logger.logit(just_for_xmlrpc=True)
    def getConnectStatus(self):
        return self.connectStatus



    def execute(self):
        # 通讯执行 not use
        pass


    @logger.logit(just_for_xmlrpc=True)
    def setLiftingLength(self, length):
        """设置升降柱总行程

        Args:
            length (int): 升降柱总行程

        Returns:
            bool: 执行结果
        """
        self.liftingLength = length
        if self.maxLimit is None:
            self.maxLimit = self.liftingLength
        return True


    @logger.logit(just_for_xmlrpc=True)
    def setVisualLimit(self, minLimit, maxLimit):
        """设置虚拟限位

        Args:
            minLimit (int): 虚拟最低限位
            maxLimit (int): 虚拟最高限位
        """
        self.minLimit = minLimit
        self.maxLimit = maxLimit
        return True


    @logger.logit(just_for_xmlrpc=False)
    def __posLimit(self, targetPos):
        effectivePos = min(self.liftingLength, targetPos)
        if self.minLimit:
            effectivePos = max(self.minLimit, effectivePos)
        if self.maxLimit:
            effectivePos = min(self.maxLimit, effectivePos)
        return effectivePos


    def testReadRegister(self):
        """
            测试是否与设备联通
        """
        return self.master.readHoldingRegisters(self.InovanceServo.ACTUAL_SPEED)[0]


    @logger.logit(just_for_xmlrpc=True)
    def currentHeight(self):
        """读取当前的高度

        Returns:
            int: 当前的高度
        """
        try:
            if self.connectStatus:
                pluse = self.master.readHoldingRegisters(self.InovanceServo.ABS_POSITION, 2)
                logger.info("pluse is %s"%str(pluse))
                if pluse is not None:
                    actualPluse = struct.pack("2H", pluse[0], pluse[1])
                    height = struct.unpack("i", actualPluse)[0] / self.PLUSE_ONE_MM
                    return int(height)
                else:
                    return -1
            else:
                return -1
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return -1


    @logger.logit(just_for_xmlrpc=True)
    def currentSpeed(self):
        """读取当前的速度

        Returns:
            int: 速度
        """
        try:
            if self.connectStatus:
                speed = self.master.readHoldingRegisters(self.InovanceServo.ACTUAL_SPEED)[0]
                if speed is not None:
                    actualSpeed = struct.unpack("h", struct.pack("H", speed))[0]
                    return round(actualSpeed / self.SPEED_CONVERTOR)
                return False
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False


    @logger.logit(just_for_xmlrpc=True)
    def currentStatus(self):
        """读取当前的状态

        Returns:
            bool: True 已连接 False 未连接
        """
        return self.connectStatus
        

    @logger.logit(just_for_xmlrpc=True)
    def currentCurrent(self):
        """当前电流

        Returns:
            int: 电流
        """
        try:
            if self.connectStatus:
                current = self.master.readHoldingRegisters(self.InovanceServo.CURRENT)[0]
                if current is not None:
                    return current  # 拿到该数据后，需要/100显示为A
                return False
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False


    @logger.logit(just_for_xmlrpc=True)
    def currentTemperature(self):
        """当前温度

        Returns:
            int: 温度
        """
        try:
            if self.connectStatus:
                temperature = self.master.readHoldingRegisters(self.InovanceServo.TEMPERATURE)[0]
                if temperature is not None:
                    return temperature
                return False
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False


    @logger.logit(just_for_xmlrpc=True)
    def errorCode(self):
        """当前错误码

        Returns:
            int: 错误码
        """
        try:
            if self.connectStatus:
                errorCode = self.master.readHoldingRegisters(self.InovanceServo.ERROR_CODE)[0]
                if errorCode is not None:
                    return errorCode
                return False
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False
        
        
    @logger.logit(just_for_xmlrpc=True)
    def getLiftingInfo(self):
        if self.connectStatus:
            height = self.currentHeight()
            speed  = self.currentSpeed()
            status = 0 if speed == 0 else 1
            return [height, speed, status]
        else:
            return False

    @logger.logit(just_for_xmlrpc=True)
    def getServoInfo(self):
        if self.connectStatus:
            current = self.currentCurrent()
            temperature = self.currentTemperature()
            errorCode = self.errorCode()
            return [current, temperature, errorCode]
        else:
            return False


    @logger.logit(just_for_xmlrpc=True)
    def move(self, pos, speed, block=True):
        """移动指令"""
        self.moveBlockStatus = True
        try:
            # 高度转换
            actualPos = self.__posLimit(pos)
            targetPluse = int(actualPos * self.PLUSE_ONE_MM)
            posValue = struct.unpack("2H", struct.pack("i", targetPluse))
            
            # 速度转换
            speed = min(speed, self.DEFAULT_MAX_SPEED)          # 200mm/s is max move speed
            actualRPM = int(speed * self.SPEED_CONVERTOR)
            
            logger.info("move actual pos %s speed %s"%(actualPos, actualRPM))
            if self.connectStatus:
                if self.errorCode() > 0:
                    self.reset()
                # 先写入一个servoon,防止not block时 visual_di的状态是多段位置, 连续写入 两个字的位置和一个字的速度
                self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.SERVO_ON])
                self.master.writeHoldingRegisters(self.InovanceServo.TARGET_POSITION, [posValue[0], posValue[1], actualRPM])
                time.sleep(0.01)
                self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.MULTI_SPEED_POS])
                while block & self.moveBlockStatus  :
                    time.sleep(0.05)
                    rtHeight = self.currentHeight()
                    errorCode = self.errorCode()
                    if rtHeight != -1 and rtHeight == actualPos:
                        self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.SERVO_ON])
                        break
                    # 有错误码,主动停止
                    if errorCode > 0:
                        self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.SERVO_ON])
                        logger.info("Fault Detected During Movement Block")
                        return False
                        
                return True
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False


    @logger.logit(just_for_xmlrpc=True)
    def jogUp(self, enable):
        """点动上升"""
        try:
            if self.connectStatus:
                if enable:
                    self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.JOG_UP])
                else:
                    self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.SERVO_ON])
                return True
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False


    @logger.logit(just_for_xmlrpc=True)
    def jogDown(self, enable):
        """电动下降"""
        try:
            if self.connectStatus:
                if enable:
                    self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.JOG_DOWN])
                else:
                    self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.SERVO_ON])
                return True
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False


    @logger.logit(just_for_xmlrpc=True)
    def stop(self):
        """停止运行"""
        self.moveBlockStatus = False
        try:
            if self.connectStatus:
                
                self.master.writeHoldingRegisters(self.InovanceServo.E_STOP, [1])
                self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.SERVO_ON])
                time.sleep(0.1)
                self.master.writeHoldingRegisters(self.InovanceServo.E_STOP, [0])
                return True
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False


    @logger.logit(just_for_xmlrpc=True)
    def reset(self):
        """重置错误状态"""
        self.moveBlockStatus = False
        try:
            if self.connectStatus:
                if self.errorCode() != 0:
                    self.master.writeHoldingRegisters(self.InovanceServo.FAULT_RESET, [1])    # 该寄存器无法直接写入 报错04
                self.master.writeHoldingRegisters(self.InovanceServo.VISUAL_DI, [self.InovanceServo.SERVO_ON])
                return True
            else:
                return False
        except Exception as e:
            traceback.print_exc()
            logger.info(e)
            return False



class ThreadXMLRpcServer(ThreadingMixIn, SimpleXMLRPCServer):
    pass


if __name__ == "__main__":

    xmlRpcServerHost = "6.0.0.10" if "EliRobot_" in os.environ.get("PWD") else "127.0.0.1"
    xmlRpcServerPort = 9120
    server = ThreadXMLRpcServer((xmlRpcServerHost, xmlRpcServerPort), allow_none=True)
    server.register_introspection_functions()
    main = ZTLifting()
    server.register_instance(main)
    logger.debug("xmlRPC has started RPCServer ip is %s..." % xmlRpcServerHost)
    server.serve_forever()
