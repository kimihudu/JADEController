package lkphan.btcontroller.jadecontroller.model;

/**
 * Created by kimiboo on 2017-10-13.
 */

public class RoverModeCmd {

     /* TODO:----- Rover mode ----- */

    public static final String ADEFAULT     = "...";
    public static final String A_TERMINATE  = "halt\r";
    public static final String ROVER_START  = "roverstart\r";
    public static final String ROVER_STOP   = "roverstop\r";
    public static final String ROVER_STAT   = "roverstat\r";
    public static final String STEP_IN      = "stepin\r";
    public static final String STEP_OVER    = "stepover\r";
    public static final String STEP_OUT     = "stepout\r";
    public static final String SPECTRO      = "spectro\r";

    /* Bluetooth LED cmd */
    public static final String BT_LED_ON     = "btledon\r";
    public static final String BT_LED_OFF    = "btledoff\r";
    public static final String SET_LEFT_MTR  = "setleftmtr #\r"; // "setleftmtr + number\r"
    public static final String SET_RIGHT_NTR = "setritemtr #\r"; // "setritemtr + number\r"

    /* Servo cmd */
    public static final String SERVO_ON         = "servoon\r";
    public static final String SERVO_OFF        = "servooff\r";
    public static final String SERVO_CENTER     = "servocntr\r";
    public static final String SERVO_CLOSE      = "servoclose\r";
    public static final String SERVO_OPEN       = "servoopen\r";
    public static final String SERVO_UP         = "servoup\r";
    public static final String SERVO_DOWN       = "servodown\r";

    /* Grip cmd */
    public static final String GRIP_SET         = "gripset #\r"; // "gripset + number\r"
    public static final String ELE_SET          = "eleset #\r"; // "eleset + number\r"
    public static final String CAM_RES          = "camres 2\r"; // "camres + number\r"

    /* EXP cmd */
    public static final String EXP1_PWR_ON  = "exp1pwron\r";
    public static final String EXP1_PWR_OFF = "exp1pwroff\r";
    public static final String EXP2_PWR_ON  = "exp2pwron\r";
    public static final String EXP2_PWR_OFF = "exp2pwroff\r";
    public static final String EXP1_GET_PWR = "exp1getpwr\r";
    public static final String EXP2_GET_PWR = "exp2getpwr\r";
}

