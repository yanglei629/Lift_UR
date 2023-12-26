package com.yanglei.LIFT.impl.i18n;

import com.ur.urcap.api.domain.system.localization.UnitType;
import com.ur.urcap.api.domain.value.simple.Length;

import java.util.Locale;
import java.util.ResourceBundle;

public class TextResource {
    //private final static String fileName = "com/ur/urcap/examples/localizationswing/impl/i18n/text/text";
    //private final static String fileName = "/i18n/text/text";
    private final static String fileName = "i18n/text/text";
    private ResourceBundle resource;

    public TextResource(Locale locale) {
        locale = locale.equals(LanguagePack.rootLanguageLocale) ? Locale.ROOT : locale;
        resource = ResourceBundle.getBundle(fileName, locale, new UTF8Control());
    }

    public String language() {
        return getStringByKey("Language");
    }

    public String programmingLanguage() {
        return getStringByKey("ProgrammingLanguage");
    }

    public String units() {
        return getStringByKey("setupUnitsTitle");
    }

    public String example() {
        return getStringByKey("UnitsExample");
    }

    public String nodeDescription() {
        return getStringByKey("NodeDescription");
    }

    public String enterValue() {
        return getStringByKey("EnterValue");
    }

    public String preview() {
        return getStringByKey("Preview");
    }

    public String notSupported() {
        return getStringByKey("NotSupported");
    }

    public String systemOfMeasurement(UnitType unitType) {
        if (unitType == UnitType.METRIC) {
            return getStringByKey("Metric_Units");
        } else {
            return getStringByKey("US_Units");
        }
    }

    public String lengthUnitName(Length.Unit unit) {
        if (unit == Length.Unit.MM) {
            return getStringByKey("Millimeter");
        } else {
            return getStringByKey("Inch");
        }
    }

    private String getStringByKey(String key) {
        try {
            return resource.getString(key);
        } catch (Exception e) {
            return "!" + key;
        }
    }

    public String ip() {
        return getStringByKey("IP");
    }


    public String connect() {
        return getStringByKey("Connect");
    }

    public String disconnect() {
        return getStringByKey("Disconnect");
    }

    public String connected() {
        return getStringByKey("Connected");
    }

    public String No_Connection() {
        return getStringByKey("No_Connection");
    }

    public String Stop() {
        return getStringByKey("Stop");
    }

    public String up() {
        return getStringByKey("Up");
    }

    public String down() {
        return getStringByKey("Down");
    }

    public String zeroCalibration() {
        return getStringByKey("Zero_Calibration");
    }

    public String perform() {
        return getStringByKey("perform_immediately");
    }

    public String targetPos() {
        return getStringByKey("target_pos");
    }

    public String currentPos() {
        return getStringByKey("current_pos");
    }

    public String status() {
        return getStringByKey("status");
    }

    public String moving() {
        return getStringByKey("moving");
    }

    public String stopped() {
        return getStringByKey("stopped");
    }

    public String argument() {
        return getStringByKey("argument");
    }

    public String jogMode() {
        return getStringByKey("jog_mode");
    }

    public String remoteMode() {
        return getStringByKey("remote_mode");
    }

    public String controlMode() {
        return getStringByKey("control_mode");
    }

    public String CancelStop() {
        return getStringByKey("cancel_stop");
    }

    public String stop() {
        return getStringByKey("stop");
    }

    public String unAchievable() {
        return getStringByKey("un_achievable");
    }

    public String connectionStatus() {
        return getStringByKey("connection_status");
    }

    public String disconnected() {
        return getStringByKey("disconnected");
    }

    public String autoActivation() {
        return getStringByKey("Auto-activation");
    }

    public String targetSpeed() {
        return getStringByKey("Target_Speed");
    }

    public String port() {
        return getStringByKey("Port");
    }

    public String stroke() {
        return getStringByKey("Stroke");
    }

    public String reset() {
        return getStringByKey("Reset");
    }

    public String highVirtualLimit() {
        return getStringByKey("High_Virtual_Limit");
    }

    public String lowVirtualLimit() {
        return getStringByKey("Low_Virtual_Limit");
    }

    public String currentSpeed() {
        return getStringByKey("current_speed");
    }

    public String currentCurrent() {
        return  getStringByKey("Current_Current");
    }

    public String currentTemperature() {
        return getStringByKey("Current_Temperature");
    }

    public String errorCode() {
        return getStringByKey("Error_Code");
    }

    public String connectionInfo() {
        return getStringByKey("Connection_Info");
    }

    public String virtualLimit() {
        return getStringByKey("Virtual_Limit");
    }

    public String liftInfo() {
        return getStringByKey("Lift_Info");
    }

    public String servoInfo() {
        return getStringByKey("Servo_Info");
    }

    public String move() {
        return getStringByKey("Move");
    }

    public String jogUp() {
        return  getStringByKey("Jog_Up");
    }

    public String jogDown() {
        return getStringByKey("Jog_Down");
    }

    public String moveTo() {
        return getStringByKey("Move_To");
    }

    public String speed() {
       return getStringByKey("Speed");
    }

    public String lift() {
        return getStringByKey("Lift");
    }
}
