package com.yanglei.LIFT.impl.installation;

import com.yanglei.LIFT.impl.LiftDaemonService;
import com.yanglei.LIFT.impl.i18n.CommandNamesResource;
import com.yanglei.LIFT.impl.i18n.LanguagePack;
import com.yanglei.LIFT.impl.i18n.TextResource;
import com.yanglei.LIFT.impl.i18n.UnitsResource;
import com.yanglei.LIFT.impl.util.ILift;
import com.yanglei.LIFT.impl.util.LiftImpl;
import com.yanglei.LIFT.impl.util.XmlRpcMyDaemonInterface;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import scriptCommunicator.ScriptExporter;
import scriptCommunicator.ScriptSender;

public class LiftInstallationNodeContribution implements InstallationNodeContribution {

    private static final String IP_KEY = "inputIP";
    private static final String DEFAULT_IP = "192.168.1.5";
    private static final Integer DEFAULT_PORT = 502;
    private final LiftInstallationNodeView view;
    private final KeyboardInputFactory keyboardFactory;
    private final InstallationAPIProvider apiProvider;
    private final ILift lift;
    private DataModel model;

    //communication scripter
    private final ScriptSender sender;
    // Instance of ScriptExporter
    // Used to extract values from URScript
    private final ScriptExporter exporter;
    public static final int PORT = 10000;
    private XmlRpcMyDaemonInterface xmlRpcDaemonInterface;
    private final LiftDaemonService daemonService;

    private final LanguagePack languagePack;
    private boolean isViewOpen = false;
    public boolean isConnected = false;

    public LiftInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, LiftInstallationNodeView view, LiftDaemonService daemonService) {
        this.apiProvider = apiProvider;
        this.keyboardFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
        this.model = model;
        this.view = view;
        this.sender = new ScriptSender();
        this.exporter = new ScriptExporter();

        this.daemonService = daemonService;
        lift = new LiftImpl("127.0.0.1", PORT);
        xmlRpcDaemonInterface = new XmlRpcMyDaemonInterface("127.0.0.1", PORT);

        languagePack = new LanguagePack(apiProvider.getSystemAPI().getSystemSettings().getLocalization());
    }

    public TextResource getTextResource() {
        return languagePack.getTextResource();
    }

    private CommandNamesResource getCommandNamesResource() {
        return languagePack.getCommandNamesResource();
    }

    private UnitsResource getUnitsResource() {
        return languagePack.getUnitsResource();
    }


    @Override
    public void openView() {
        /*// i18n
        view.setIpLabel(createIPString());
        view.setConnectBtn(createConnectString());
        view.setDisconnectBtn(createDisconnectString());
        view.setConnectStatusLabel(getTextResource().No_Connection());
        view.setControlModeLabel(getTextResource().controlMode() + ":");
        view.setUpBtn(getTextResource().up());
        view.setDownBtn(getTextResource().down());
        view.setZeroCalibBtn(getTextResource().zeroCalibration());
        view.setStopBtn(getTextResource().Stop());
        view.setCancelStopBtn(getTextResource().CancelStop());

        //lif status
        view.setStatusText(getTextResource().status());

        view.setCurrentPosLabel(getTextResource().currentPos() + ":");

        view.setMovingStatus(getTextResource().status() + ":");

        view.showIP(getIP());

        //monitor connection
        isViewOpen = true;

        *//*refreshConnectionState();
        refreshMode();*//*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isViewOpen) {
                    refreshConnectionState();
                    if (isConnected) {
                        refreshMode();
                        updateUI();
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();*/

        view.showIP(getIP());
        view.showPort(getPort());
        view.showLowVirtualLimit(getLowVirtualLimit());
        view.showHighVirtualLimit(getHighVirtualLimit());
    }

    //refresh connection state
    private void refreshConnectionState() {
        Integer current_pos = getXmlRpcMyDaemonInterface().get_current_pos();
        Integer running_status = getXmlRpcMyDaemonInterface().get_running_status();
        //if (current_pos == null || current_pos == -1) {
        if (running_status == null || running_status == -1) {
            view.setDisconnect(-1, null);
            isConnected = false;
        } else {
            view.setConnected();
            isConnected = true;
        }
    }


    //refresh lift data
    private void updateUI() {
        view.setCurrentPosLabel(getTextResource().currentPos() + ":" + getCurrentPos() + "mm");

        int value = getMovingStatus();
        if (value == 1) {
            view.setMovingStatus(getTextResource().status() + ":" + getTextResource().moving());
        } else {
            view.setMovingStatus(getTextResource().status() + ":" + getTextResource().stopped());
        }
    }

    public int getTargetPos() {
        int returnValue = getLiftInstance().currentHeight();
        return returnValue;
    }

    public int getCurrentPos() {
        int returnValue = getLiftInstance().currentHeight();
        return returnValue;
    }

    public int getMovingStatus() {
        int returnValue = getLiftInstance().currentStatus();
        return returnValue;
    }

    private String createDisconnectString() {
        return getTextResource().disconnect();
    }

    private String createIPString() {
        return getTextResource().ip() + ":";
    }

    private String createConnectString() {
        return getTextResource().connect();
    }

    @Override
    public void closeView() {
        isViewOpen = false;
    }

    public boolean isDefined() {
        return !getIP().isEmpty();
    }

    @Override
    public void generateScript(ScriptWriter writer) {
    }

    //	get ip address
    public String getIP() {
        return model.get(IP_KEY, DEFAULT_IP);
    }

    public void setIP(String msg) {
        model.set(IP_KEY, msg);
    }

    public void moveUp(boolean b) {
        xmlRpcDaemonInterface.lift_up(b);
    }

    public void moveDown(boolean b) {
        xmlRpcDaemonInterface.lift_down(b);
    }

    public void calibrate() {
        xmlRpcDaemonInterface.calibrate();
    }

    public ILift getLiftInstance() {
        return lift;
    }

    public void setMode(String selected) {
        this.model.set("MODE", selected);
    }

    public void setAutoActivation(boolean b) {
        this.model.set("AUTO_ACTIVATION", b);
    }

    public boolean getAutoActivation() {
        return (boolean) this.model.get("AUTO_ACTIVATION", false);
    }

    public XmlRpcMyDaemonInterface getXmlRpcMyDaemonInterface() {
        return this.xmlRpcDaemonInterface;
    }

    public KeyboardInputFactory getKeyboardFactory() {
        return this.keyboardFactory;
    }


    public void disConnect() {
        boolean result = getLiftInstance().disConnect();
    }

    public void stopLift() {
        getLiftInstance().stop();
    }

    public Integer getPort() {
        return model.get("PORT", DEFAULT_PORT);
    }

    public void setPort(Integer value) {
        model.set("PORT", value);
    }

    public Integer getLowVirtualLimit() {
        return model.get("Low_Virtual_Limit", 0);
    }

    public void setLowVirtualLimit(Integer value) {
        this.model.set("Low_Virtual_Limit", value);
    }

    public Integer getHighVirtualLimit() {
        return model.get("High_Virtual_Limit", 0);
    }

    public void setHighVirtualLimit(Integer value) {
        this.model.set("High_Virtual_Limit", value);
    }
}
