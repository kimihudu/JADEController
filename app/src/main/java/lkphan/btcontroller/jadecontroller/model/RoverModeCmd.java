package lkphan.btcontroller.jadecontroller.model;

/**
 * Created by kimiboo on 2017-10-13.
 */

public class RoverModeCmd {

     /* TODO:----- Rover mode ----- */

    public final static String ADEFAULT     = "...";
    public final static String A_TERMINATE  = "halt\r";
    public final static String ROVER_START  = "roverstart\r";
    public final static String ROVER_STOP   = "roverstop\r";
    public final static String ROVER_STAT   = "roverstat\r";
    public final static String STEP_IN      = "stepin\r";
    public final static String STEP_OVER    = "stepover\r";
    public final static String STEP_OUT     = "stepout\r";
    public final static String SPECTRO      = "spectro\r";

    /* Bluetooth LED cmd */
    public final static String BT_LED_ON     = "btledon\r";
    public final static String BT_LED_OFF    = "btledoff\r";
    public final static String SET_LEFT_MTR  = "setleftmtr #\r"; // "setleftmtr + number\r"
    public final static String SET_RIGHT_NTR = "setritemtr #\r"; // "setritemtr + number\r"

    /* Servo cmd */
    public final static String SERVO_ON         = "servoon\r";
    public final static String SERVO_OFF        = "servooff\r";
    public final static String SERVO_CENTER     = "servocntr\r";
    public final static String SERVO_CLOSE      = "servoclose\r";
    public final static String SERVO_OPEN       = "servoopen\r";
    public final static String SERVO_UP         = "servoup\r";
    public final static String SERVO_DOWN       = "servodown\r";

    /* Grip cmd */
    public final static String GRIP_SET         = "gripset #\r"; // "gripset + number\r"
    public final static String ELE_SET          = "eleset #\r"; // "eleset + number\r"
    public final static String CAM_RES          = "camres 1\r"; // "camres + number\r"

    /* EXP cmd */
    public final static String EXP1_PWR_ON  = "exp1pwron\r";
    public final static String EXP1_PWR_OFF = "exp1pwroff\r";
    public final static String EXP2_PWR_ON  = "exp2pwron\r";
    public final static String EXP2_PWR_OFF = "exp2pwroff\r";
    public final static String EXP1_GET_PWR = "exp1getpwr\r";
    public final static String EXP2_GET_PWR = "exp2getpwr\r";
}
