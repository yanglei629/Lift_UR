package com.yanglei.LIFT.impl.util;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LiftImpl implements ILift{

    private final XmlRpcClient client;

    public LiftImpl(String host, int port) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(3);
        try {
            config.setServerURL(new URL("http://" + host + ":" + port + "/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        config.setConnectionTimeout(1000);
        client = new XmlRpcClient();
        client.setConfig(config);
    }

    @Override
    public boolean connect(String ip, Integer port, Integer slaveId) {
        ArrayList<Object> args = new ArrayList<Object>();
        args.add(ip);
        args.add(port);
        args.add(1);
        Object result = null;
        try {
            result = client.execute("connect", args);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean disConnect() {
        ArrayList<String> args = new ArrayList<String>();
        Object result = null;
        try {
            result = client.execute("disConnect", args);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean getConnectionStatus() {
        ArrayList<String> args = new ArrayList<String>();
        Object result = null;
        try {
            result = client.execute("getConnectionStatus", args);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean setVirtualLimit(Integer min, Integer max) {
        ArrayList<String> args = new ArrayList<String>();
        Object result = null;
        try {
            result = client.execute("setVisualLimit", args);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean move(Integer pos, Integer speed) {
        ArrayList<Integer> args = new ArrayList<Integer>();
        args.add(pos);
        args.add(speed);
        Object result = null;
        try {
            result = client.execute("move", args);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean jogUp(boolean enable) {
        ArrayList<Boolean> args = new ArrayList<Boolean>();
        args.add(enable);
        Object result = null;
        try {
            result = client.execute("jogUp", args);
        }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean jogDown(boolean enable) {
        ArrayList<Boolean> args = new ArrayList<Boolean>();
        args.add(enable);
        Object result = null;
        try {
            result = client.execute("jogDown", args);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean stop() {
        ArrayList<Boolean> args = new ArrayList<Boolean>();
        Object result = null;
        try {
            result = client.execute("stop", args);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public boolean reset() {
        ArrayList<Boolean> args = new ArrayList<Boolean>();
        Object result = null;
        try {
            result = client.execute("reset", args);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (Boolean) result;
    }

    @Override
    public ArrayList<Integer> getLiftingInfo() {
        ArrayList<Boolean> args = new ArrayList<Boolean>();
        Object result = null;
        try {
            result = client.execute("getLiftingInfo", args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Integer> getServoInfo() {
        ArrayList<Boolean> args = new ArrayList<Boolean>();
        Object result = null;
        try {
            result = client.execute("getServoInfo", args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public Integer currentHeight() {
        return null;
    }

    @Override
    public Integer currentSpeed() {
        return null;
    }

    @Override
    public Integer currentStatus() {
        return null;
    }
}
