package com.yanglei.LIFT.impl.program;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.yanglei.LIFT.impl.i18n.LanguagePack;
import com.yanglei.LIFT.impl.i18n.TextResource;
import com.yanglei.LIFT.impl.installation.LiftInstallationNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;

import scriptCommunicator.ScriptExporter;
import scriptCommunicator.ScriptSender;

public class LiftProgramNodeContribution implements ProgramNodeContribution {

    private final ProgramAPI programAPI;
    private final UndoRedoManager undoRedoManager;
    private static final String POSKEY = "pos";
    private static final int DEFAULT_POS = 0;

    // Used to send a URScript for execution
    private final ScriptSender sender;
    // Instance of ScriptExporter
    // Used to extract values from URScript
    private final ScriptExporter exporter;

    private final LiftProgramNodeView view;
    private final DataModel model;
    private Timer timer;

    public KeyboardInputFactory getKeyboardFactory() {
        return keyboardFactory;
    }

    private final KeyboardInputFactory keyboardFactory;
    private final InputValidationFactory keyboardInputValidationFactory;

    //UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
    private Timer uiTimer;
    private final LanguagePack languagePack;

    public LiftProgramNodeContribution(ProgramAPIProvider apiProvider, LiftProgramNodeView view, DataModel model) {
        this.programAPI = apiProvider.getProgramAPI();
        this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();
        this.keyboardFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();

        keyboardInputValidationFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();

        this.view = view;
        this.model = model;
        this.sender = new ScriptSender();
        this.exporter = new ScriptExporter();

        languagePack = new LanguagePack(apiProvider.getSystemAPI().getSystemSettings().getLocalization());

        setPos(0);
        setSpeed(10);
    }

    public InputValidationFactory getKeyboardInputValidationFactory() {
        return keyboardInputValidationFactory;
    }

    public TextResource getTextResource() {
        return languagePack.getTextResource();
    }

    @Override
    public void openView() {
        // i18n
        view.setLiftInfoPanel(getTextResource().liftInfo());
        view.setMovePanel(getTextResource().move());
        view.setTargetHeightLabel(getTextResource().targetPos());
        view.setTargetSpeedLabel(getTextResource().targetSpeed());
        view.setExecuteBtn(getTextResource().perform());
        view.setStopBtn(getTextResource().stop());
        view.setResetBtn(getTextResource().reset());
        view.setJogUpBtn(getTextResource().jogUp());
        view.setJogDownBtn(getTextResource().jogDown());
        view.setHeightFeedbackLabel(getTextResource().currentPos());
        view.setSpeedFeedbackLabel(getTextResource().currentSpeed());
        view.setStatusFeedbackLabel(getTextResource().status());

        view.reDefineComponent(this);
        view.showPos(getPos());
        view.showSpeed(getSpeed());
        view.setConnect(getInstallation().isConnect());

        if (this.timer == null) {
            this.timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    boolean connectionStatus = getInstallation().getConnectionStatus();
                    if (connectionStatus) {
                        try {
                            view.setConnect(true);
                            ArrayList<Double> liftingInfo = getInstallation().getLiftingInfo();

                            Double height = liftingInfo.get(0);
                            Double speed = liftingInfo.get(1);
                            Double status = liftingInfo.get(2);
                            view.refreshState(LiftProgramNodeContribution.this,true, height, speed, status);
                        } catch (Exception e) {
                        }
                    } else {
                        view.setConnect(false);
                        view.refreshState(LiftProgramNodeContribution.this,false, 0.0, 0.0, 0.0);
                    }
                }
            }, 0, 2000);
        }
    }


    @Override
    public void closeView() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    @Override
    public String getTitle() {
        return getTextResource().moveTo() + ":" + getPos() + "mm" + " " + getTextResource().speed() + ":" + getSpeed() + "mm/s";
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        // build rpc client
        scriptWriter.appendLine("lift=rpc_factory(\"xmlrpc\",\"http://127.0.0.1:9120/\")");
        scriptWriter.appendLine(String.format("lift.move(%1$s, %2$s)", getPos(), getSpeed()));

        /*scriptWriter.appendLine("while lift.currentHeight() != " + getPos() + ":");
        scriptWriter.appendLine("    sleep(1)");
        scriptWriter.appendLine("end");*/
    }

    public int getPos() {
        return model.get(POSKEY, DEFAULT_POS);
    }

    private LiftInstallationNodeContribution getInstallation() {
        return programAPI.getInstallationNode(LiftInstallationNodeContribution.class);
    }

    public void setPos(Integer pos) {
        programAPI.getUndoRedoManager().recordChanges(() -> {
            model.set(POSKEY, pos);
        });
    }

    public LiftInstallationNodeContribution getInstalltion() {
        return this.programAPI.getInstallationNode(LiftInstallationNodeContribution.class);
    }

    public Integer getSpeed() {
        return model.get("Speed", DEFAULT_POS);
    }

    public void setSpeed(Integer value) {
        programAPI.getUndoRedoManager().recordChanges(() -> {
            model.set("Speed", value);
        });
    }

    public void execute() {
        getInstalltion().getLiftInstance().move(getPos(), getSpeed(), false);
    }

    public void stopLift() {
        getInstalltion().stopLift();
    }

    public void reset() {
        getInstallation().resetLift();
    }

    public void jogUp(boolean b) {
        getInstallation().jogUp(b);
    }

    public void jogDown(boolean b) {
        getInstallation().jogDown(b);
    }
}
