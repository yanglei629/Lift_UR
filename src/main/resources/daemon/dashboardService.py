#!/usr/bin/env python2

import socket
import threading
import time
from pluginLog import Log

logger = Log("daemonService")


class DashboardService:
    def __init__(self, ip="127.0.0.1"):
        # self.HOST = '127.0.0.1'
        self.HOST = ip
        self.PORT = 29999
        self.dash = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        self.current_program_state = None
        self.pre_program_state = None
        self.on_connect = []
        self.monitor = False

    def get_program_state(self):
        try:
            # outdata = 'programState\n'
            outdata = 'running\n'
            self.dash.send(outdata.encode())

            indata = self.dash.recv(1024)
            print('recv: ' + indata.decode())
            logger.info('recv: ' + indata.decode())
            # return indata.decode().split(' ')[0]
            return indata.decode().split(':')[1].strip()
        except Exception as e:
            # print(str(e))
            logger.info("[Error] get_program_state : " + str(e))
            # return get_program_state()
            return -1

    def monitor_program_state(self):
        try:
            self.dash.connect((self.HOST, self.PORT))
            recv = self.dash.recv(1024)
            print('recv: ' + recv.decode())
        except:
            print("[WARNING] build socket with robot failed.....")

        print("[WARING] Start Monitor Program State...........")
        self.pre_program_state = self.get_program_state()
        while self.monitor:
            res = self.get_program_state()
            if res == -1:
                print("[WARING] get program state failed")
                time.sleep(1)
                continue
            self.current_program_state = res

            if self.current_program_state == 'false' and self.pre_program_state == 'true':
                print("[Warning] Program Stopped")
                self.onStop()

            self.pre_program_state = self.current_program_state
            time.sleep(1)
        print("[WARNING] Monitor Thread Exit.................")

    def onStop(self):
        for listener in self.on_connect:
            try:
                listener()
            except Exeption as e:
                print(str(e))

    def add_listener(self, listener):
        self.on_connect.append(listener)

    def start_listen(self):
        self.monitor = False
        time.sleep(1)
        self.monitor = True
        threading.Thread(target=self.monitor_program_state, args=()).start()

    def stop_listen(self):
        self.monitor = False


def onStop():
    print("stop")


if __name__ == '__main__':
    dashboardService = DashboardService("192.168.95.167")
    dashboardService.add_listener(onStop)
    dashboardService.start_listen()
