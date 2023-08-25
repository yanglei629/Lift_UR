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
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

import scriptCommunicator.ScriptExporter;
import scriptCommunicator.ScriptSender;

public class LiftProgramNodeContribution implements ProgramNodeContribution {

    private final ProgramAPI programAPI;
    private final UndoRedoManager undoRedoManager;
    private static final String POSKEY = "pos";
    private static final int DEFAULT_POS = 0;

    private static final String DISPOSKEY = "DISPOSKEY";

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
    private Timer uiTimer;        //UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
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
        setSpeed(100);
    }

    public InputValidationFactory getKeyboardInputValidationFactory() {
        return keyboardInputValidationFactory;
    }

    private TextResource getTextResource() {
        return languagePack.getTextResource();
    }

    @Override
    public void openView() {
        view.reDefineComponent();
        view.showPos(getPos());
        view.showSpeed(getSpeed());

        if (this.timer ==null) {
            this.timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    boolean connectionStatus = getInstallation().getConnectionStatus();
                    if (connectionStatus) {
                        ArrayList<Integer> liftingInfo = getInstallation().getLiftingInfo();

                        Integer height = liftingInfo.get(0);
                        Integer speed = liftingInfo.get(1);
                        Integer status = liftingInfo.get(2);
                        view.refreshState(true, height, speed, status);
                    } else {
                        view.refreshState(false, 0, 0, 0);
                    }
                }
            }, 0, 2000);
        }
    }


    @Override
    public void closeView() {
        if (this.timer!=null){
            this.timer.cancel();
            this.timer = null;
        }
    }

    @Override
    public String getTitle() {
        return "BYLift: Pos : " + (model.isSet(POSKEY) ? getPos() : "");
        //return "BYLift: Pos : " + getPos();
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        //build rpc client
        scriptWriter.appendLine("lift=rpc_factory(\"xmlrpc\",\"http://127.0.0.1:9120/\")");
        scriptWriter.appendLine(String.format("lift.move($1%s, $2%s)",getPos(),getSpeed()));

        scriptWriter.appendLine("while lift.currenHeight() != " + getPos() + ":");
        scriptWriter.appendLine("    sleep(1)");
        scriptWriter.appendLine("end");
    }

    public int getPos() {
        return model.get(POSKEY, DEFAULT_POS);
    }

    public void onPosSelection(final int pos) {
        undoRedoManager.recordChanges(new UndoableChanges() {
            @Override
            public void executeChanges() {
                model.set(POSKEY, pos);
            }
        });
    }

    private LiftInstallationNodeContribution getInstallation() {
        return programAPI.getInstallationNode(LiftInstallationNodeContribution.class);
    }

    public void setPos(Integer pos) {
        // model.set(POSKEY, pos);
        programAPI.getUndoRedoManager().recordChanges(()->{
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
        programAPI.getUndoRedoManager().recordChanges(()->{
            model.set("Speed", value);
        });
    }

    public void execute() {
        getInstalltion().getLiftInstance().move(getPos(),getSpeed());
    }

    public void stopLift() {
        getInstalltion().stopLift();
    }
}
