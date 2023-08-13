package com.yanglei.LIFT.impl.util;

public interface ILift{
    boolean connect(String ip, Integer port, Integer slaveId);

    boolean disConnect();

    boolean getConnectionStatus();

    boolean setVirtualLimit(Integer min, Integer max);

    boolean move(Integer pos, Integer speed);

    boolean jogUp(boolean enable);

    boolean jogDown(boolean enable);

    boolean stop();

    boolean reset();

    Integer currentHeight();

    Integer currentSpeed();

    Integer currentStatus();
}
