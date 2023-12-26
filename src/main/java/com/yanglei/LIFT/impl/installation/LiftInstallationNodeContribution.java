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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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

    // Instance of ScriptExporter, Used to extract values from URScript
    private final ScriptExporter exporter;
    public static final int PORT = 9120;
    private XmlRpcMyDaemonInterface xmlRpcDaemonInterface;
    private final LiftDaemonService daemonService;

    private final LanguagePack languagePack;
    private Timer timer;
    private boolean isConnect;

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

        setIP("192.168.1.12");
        setPort(502);
        setStroke(500);
        setLowVirtualLimit(0);
        setHighVirtualLimit(getStroke());
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
        if (this.timer == null) {
            this.timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    boolean connectionStatus = getConnectionStatus();
                    if (connectionStatus) {
                        isConnect = true;
                        try {
                            ArrayList<Double> liftingInfo = getLiftingInfo();
                            ArrayList<Double> servoInfo = getServoInfo();

                            Double height = liftingInfo.get(0);
                            Double speed = liftingInfo.get(1);
                            Double status = liftingInfo.get(2);

                            Double current = servoInfo.get(0) / 100;
                            Double temperature = servoInfo.get(1);
                            Double errorCode = servoInfo.get(2);

                            view.refreshState(true, height, speed, status, current, temperature, errorCode);
                            view.setConnected(true);
                        } catch (Exception e) {
                        }
                    } else {
                        isConnect = false;
                        /*ArrayList<Double> liftingInfo = getLiftingInfo();
                        ArrayList<Double> servoInfo = getServoInfo();*/

                        try {
                            view.refreshState(false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
                            view.setConnected(false);
                        } catch (Exception e) {
                        }
                    }
                }
            }, 0, 2000);
        }

        view.showIP(getIP());
        view.showPort(getPort());
        view.showLowVirtualLimit(getLowVirtualLimit());
        view.showHighVirtualLimit(getHighVirtualLimit());
    }

    private ArrayList<Double> getServoInfo() {
        return lift.getServoInfo();
    }

    public boolean getConnectionStatus() {
        return lift.getConnectStatus();
    }

    public ArrayList<Double> getLiftingInfo() {
        return lift.getLiftingInfo();
    }

    @Override
    public void closeView() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
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

    public ILift getLiftInstance() {
        return lift;
    }

    public XmlRpcMyDaemonInterface getXmlRpcMyDaemonInterface() {
        return this.xmlRpcDaemonInterface;
    }

    public KeyboardInputFactory getKeyboardFactory() {
        return this.keyboardFactory;
    }

    public void stopLift() {
        lift.stop();
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

    public void connect() {
        this.lift.setStroke(getStroke());
        this.lift.setVirtualLimit(getLowVirtualLimit(), getHighVirtualLimit());
        this.lift.connect(getIP(), getPort(), 1);
    }

    public void resetLift() {
        this.lift.reset();
    }

    public void jogUp(boolean b) {
        this.lift.jogUp(b);
    }

    public void jogDown(boolean b) {
        this.lift.jogDown(b);
    }

    public Integer getStroke() {
        return this.model.get("Stroke", 500);
    }

    public void setStroke(Integer value) {
        this.model.set("Stroke", value);
    }

    public void disConnect() {
        this.lift.disConnect();
    }

    public boolean isConnect() {
        return this.isConnect;
    }
}
