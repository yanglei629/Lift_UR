package com.yanglei.LIFT.impl.util;

import java.util.ArrayList;

public interface ILift{
    boolean connect(String ip, Integer port, Integer slaveId);

    boolean disConnect();

    boolean getConnectStatus();

    boolean setVirtualLimit(Integer min, Integer max);

    boolean move(Integer pos, Integer speed, boolean block);

    boolean jogUp(boolean enable);

    boolean jogDown(boolean enable);

    boolean stop();

    boolean reset();

    void setStroke(Integer stoke);

    ArrayList<Double> getLiftingInfo();
    ArrayList<Double> getServoInfo();

    Integer currentHeight();

    Integer currentSpeed();

    Integer currentStatus();
}
