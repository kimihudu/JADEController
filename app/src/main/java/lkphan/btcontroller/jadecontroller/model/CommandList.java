package lkphan.btcontroller.jadecontroller.model;

/**
 * Created by kimiboo on 2017-09-20.
 */

public class CommandList {

    /* TODO:----- Command mode ----- */

    /* General cmd */
    public static final String A_HELLO        = "*\r";
    public static final String A_TERMINATE    = "halt\r";
    public static final String CURRENT_STAT    = "Script Executing\r";

    /* Device info cmd */
    public static final String B_FW_VER       = "ver\r";
    public static final String B_HW_VER       = "hwver\r";
    public static final String B_MAC_ADD      = "btser\r";
    public static final String B_NAME         = "getname\r";
    public static final String B_PROC_TYPE    = "proctype\r";
    public static final String B_GET_BUTTON   = "getbutton\r";
    public static final String B_BAT_LV       = "batlevel\r";


    /* Features status cmd */
    public static final String C_STATUS       = "status\r";
    public static final String C_STATUS_MSG   = "statusmsg\r";
    public static final String C_ERR_MSG_NXT  = "errmsgnxt\r";
    public static final String C_USB_STATUS   = "usbstatus\r";
    public static final String C_SWAP_STATUS  = "swapstatus\r";
    public static final String C_BT_STATUS    = "rssi\r";
    public static final String C_CAM_STATUS   = "camstatus\r";

    /* Files & storage cmd */
    public static final String D_DIR_WILDCARD     = "dir [*.*]\r";
    public static final String D_DIR_NXT          = "dirnxt\r";
    public static final String D_FILE_MAP         = "filemap\r";
    public static final String D_FILE_MAP_NXT     = "filemapnxt\r";
    public static final String D_STORGE_AVAIL     = "avail\r";
    public static final String D_DEL_FILE         = "del fileName.e\r"; //---
    public static final String D_DEL_ALL          = "deleteall\r";
    public static final String D_CLEAR            = "garbage\r";
    public static final String D_META_FILE        = "meta fileName.s\r"; //---
    public static final String D_META_NXT         = "metanxt\r";
    public static final String D_DOWNLOAD_FILE    = "download fileName.e size\r"; //---
    public static final String D_DOWNLOAD_BLOCK16 = "block ## ## ... ##\r";
    public static final String D_DOWNLOAD_BLOCK48 = "block @#data...\r";

    /* The Jade Robot’s MCU cmd */
    public static final String E_FLASH_WRITE      = "flashwrite hexAddress\r";
    public static final String E_FLASH_BLOCK16    = "flashblock ## ## ... ##\r";
    public static final String E_FLASH_READ       = "flashread hexAddress\r";
    public static final String E_BLOCK1_CRC       = "block1crc\r";
    public static final String E_SWAP_ON          = "swapon\r";
    public static final String E_DO_SWAP          = "do_swap\r";
    public static final String E_SWAP_OFF         = "swapoff\r";
    public static final String E_DO_RESET         = "do_reset\r";
    public static final String E_CUR_COPY         = "cur_copy\r";

    /* Dump file cmd */
    public static final String F_DUMP_FILE    = "dump fileName.e\r"; //---
    public static final String F_HEX_DUMP_NXT = "hexdumpnxt\r";

    /* Sensors cmd */
    public static final String G_IR_DIST_SENSORs  = "irsense\r";
    public static final String G_LIGHT_SENSORS    = "lightsense\r";
    public static final String G_LINE_SENSORS     = "linesense\r";

    /* Audio cmd */
    public static final String H_PLAY_FILE    = "play fileName.w\r";
    public static final String H_SET_VOL      = "setvol #\r";
    public static final String H_GET_VOL      = "gevol\r";

    /* OLED display cmd */
    public static final String I_SHOWBMP_FILE = "showbmp fileName.b\r"; //---
    public static final String I_SCREEN_DUMP  = "screendump\r";

    /* Panel cmd */
    public static final String J_STOP_PANEL_DRIVER    = "paneloff\r";
    public static final String J_PANELLOAD_FILE       = "panelload fileName.p\r";
    public static final String J_PANEL_STATE          = "panelstate\r";
    public static final String J_PANEL_UPDATED        = "panelgupdt\r";
    public static final String J_PANEL_CLEAR_UPDATED  = "panelcupdt\r";

    /* Move cmd */
    public static final String M_FORWARD          = "forward\r";
    public static final String M_REVERSE          = "reverse\r";
    public static final String M_LEFT             = "left\r";
    public static final String M_RIGHT            = "right\r";
    public static final String M_MTR_STOP         = "mtrstop\r";

    /* Camera cmd */
    public static final String CAM_ON           = "camon\r";
    public static final String CAM_OFF          = "camoff\r";
    public static final String CAM_SHOOT        = "camshoot\r";

    /* Script file cmd */
    public static final String P_LOAD_FILE        = "load fileName.s\r"; //---
    public static final String P_RESET            = "reset\r";
    public static final String P_RESUME           = "resume\r";
    public static final String P_VAR_DUMP         = "vardump\r";
    public static final String P_VAR_DUMP_NXT     = "vardumpnxt\r";
    public static final String P_STACK_DUMP       = "stackdump\r";
    public static final String P_STACK_DUMP_NXT   = "stackdumpn\r";
    public static final String P_BREAKSET_FILE    = "breakset fileName.s:#\r"; //---
    public static final String P_BREAKCLEAR_FILE  = "breakclear fileName.s:#\r"; //---
    public static final String P_BREAK_LIST       = "breaklist\r";
    public static final String P_BREAK_NEXT       = "breaknext\r";
    public static final String P_WRITE_VAR        = "writevar variableName:newValue\r";
    public static final String P_EXPSENDRX        = "expsendrx Device:Register:sendLength:receiveLength:## ## ... ##\r"; //---

    /* TODO:----- Rover mode ----- */

    public static final String R_ROVER_START  = "roverstart\r";
    public static final String R_ROVER_STOP   = "roverstop\r";
    public static final String R_ROVER_STAT   = "roverstat\r";
    public static final String R_STEP_IN      = "stepin\r";
    public static final String R_STEP_OVER    = "stepover\r";
    public static final String R_STEP_OUT     = "stepout\r";
    public static final String R_SPECTRO      = "spectro\r";

    /* Bluetooth LED cmd */
    public static final String R_BT_LED_ON     = "btledon\r";
    public static final String K_BT_LED_OFF    = "btledoff\r";

    public static final String L_SET_LEFT_MTR  = "setleftmtr ##\r";
    public static final String L_SET_RIGHT_NTR = "setritemtr ##\r";

    /* Servo cmd */
    public static final String SERVO_ON         = "servoon\r";
    public static final String SERVO_OFF        = "servooff\r";
    public static final String SERVO_CENTER     = "servocntr\r";
    public static final String SERVO_CLOSE      = "servoclose\r";
    public static final String SERVO_OPEN       = "servoopen\r";
    public static final String SERVO_UP         = "servoup\r";
    public static final String SERVO_DOWN       = "servodown\r";

    /* Grip cmd */
    public static final String N_GRIP_SET         = "gripset ##\r";
    public static final String N_ELE_SET          = "eleset ##\r";

    public static final String O_CAM_RES          = "camres #\r";

    /* EXP cmd */
    public static final String Q_EXP1_PWR_ON  = "exp1pwron\r";
    public static final String Q_EXP1_PWR_OFF = "exp1pwroff\r";
    public static final String Q_EXP2_PWR_ON  = "exp2pwron\r";
    public static final String Q_EXP2_PWR_OFF = "exp2pwroff\r";
    public static final String Q_EXP1_GET_PWR = "exp1getpwr\r";
    public static final String Q_EXP2_GET_PWR = "exp2getpwr\r";

}
