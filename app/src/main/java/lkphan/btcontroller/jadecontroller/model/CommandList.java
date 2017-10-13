package lkphan.btcontroller.jadecontroller.model;

/**
 * Created by kimiboo on 2017-09-20.
 */

public class CommandList {

    /* TODO:----- Command mode ----- */

    /* General cmd */
    public final static String A_HELLO        = "*\r";
    public final static String A_TERMINATE    = "halt\r";
    public final static String CURRENT_STAT    = "Script Executing\r";

    /* Device info cmd */
    public final static String B_FW_VER       = "ver\r";
    public final static String B_HW_VER       = "hwver\r";
    public final static String B_MAC_ADD      = "btser\r";
    public final static String B_NAME         = "getname\r";
    public final static String B_PROC_TYPE    = "proctype\r";
    public final static String B_GET_BUTTON   = "getbutton\r";
    public final static String B_BAT_LV       = "batlevel\r";


    /* Features status cmd */
    public final static String C_STATUS       = "status\r";
    public final static String C_STATUS_MSG   = "statusmsg\r";
    public final static String C_ERR_MSG_NXT  = "errmsgnxt\r";
    public final static String C_USB_STATUS   = "usbstatus\r";
    public final static String C_SWAP_STATUS  = "swapstatus\r";
    public final static String C_BT_STATUS    = "rssi\r";
    public final static String C_CAM_STATUS   = "camstatus\r";

    /* Files & storage cmd */
    public final static String D_DIR_WILDCARD     = "dir [*.*]\r";
    public final static String D_DIR_NXT          = "dirnxt\r";
    public final static String D_FILE_MAP         = "filemap\r";
    public final static String D_FILE_MAP_NXT     = "filemapnxt\r";
    public final static String D_STORGE_AVAIL     = "avail\r";
    public final static String D_DEL_FILE         = "del fileName.e\r"; //---
    public final static String D_DEL_ALL          = "deleteall\r";
    public final static String D_CLEAR            = "garbage\r";
    public final static String D_META_FILE        = "meta fileName.s\r"; //---
    public final static String D_META_NXT         = "metanxt\r";
    public final static String D_DOWNLOAD_FILE    = "download fileName.e size\r"; //---
    public final static String D_DOWNLOAD_BLOCK16 = "block ## ## ... ##\r";
    public final static String D_DOWNLOAD_BLOCK48 = "block @#data...\r";

    /* The Jade Robot’s MCU cmd */
    public final static String E_FLASH_WRITE      = "flashwrite hexAddress\r";
    public final static String E_FLASH_BLOCK16    = "flashblock ## ## ... ##\r";
    public final static String E_FLASH_READ       = "flashread hexAddress\r";
    public final static String E_BLOCK1_CRC       = "block1crc\r";
    public final static String E_SWAP_ON          = "swapon\r";
    public final static String E_DO_SWAP          = "do_swap\r";
    public final static String E_SWAP_OFF         = "swapoff\r";
    public final static String E_DO_RESET         = "do_reset\r";
    public final static String E_CUR_COPY         = "cur_copy\r";

    /* Dump file cmd */
    public final static String F_DUMP_FILE    = "dump fileName.e\r"; //---
    public final static String F_HEX_DUMP_NXT = "hexdumpnxt\r";

    /* Sensors cmd */
    public final static String G_IR_DIST_SENSORs  = "irsense\r";
    public final static String G_LIGHT_SENSORS    = "lightsense\r";
    public final static String G_LINE_SENSORS     = "linesense\r";

    /* Audio cmd */
    public final static String H_PLAY_FILE    = "play fileName.w\r";
    public final static String H_SET_VOL      = "setvol #\r";
    public final static String H_GET_VOL      = "gevol\r";

    /* OLED display cmd */
    public final static String I_SHOWBMP_FILE = "showbmp fileName.b\r"; //---
    public final static String I_SCREEN_DUMP  = "screendump\r";

    /* Panel cmd */
    public final static String J_STOP_PANEL_DRIVER    = "paneloff\r";
    public final static String J_PANELLOAD_FILE       = "panelload fileName.p\r";
    public final static String J_PANEL_STATE          = "panelstate\r";
    public final static String J_PANEL_UPDATED        = "panelgupdt\r";
    public final static String J_PANEL_CLEAR_UPDATED  = "panelcupdt\r";

    /* Move cmd */
    public final static String M_FORWARD          = "forward\r";
    public final static String M_REVERSE          = "reverse\r";
    public final static String M_LEFT             = "left\r";
    public final static String M_RIGHT            = "right\r";
    public final static String M_MTR_STOP         = "mtrstop\r";

    /* Camera cmd */
    public final static String CAM_ON           = "camon\r";
    public final static String CAM_OFF          = "camoff\r";
    public final static String CAM_SHOOT        = "camshoot\r";

    /* Script file cmd */
    public final static String P_LOAD_FILE        = "load fileName.s\r"; //---
    public final static String P_RESET            = "reset\r";
    public final static String P_RESUME           = "resume\r";
    public final static String P_VAR_DUMP         = "vardump\r";
    public final static String P_VAR_DUMP_NXT     = "vardumpnxt\r";
    public final static String P_STACK_DUMP       = "stackdump\r";
    public final static String P_STACK_DUMP_NXT   = "stackdumpn\r";
    public final static String P_BREAKSET_FILE    = "breakset fileName.s:#\r"; //---
    public final static String P_BREAKCLEAR_FILE  = "breakclear fileName.s:#\r"; //---
    public final static String P_BREAK_LIST       = "breaklist\r";
    public final static String P_BREAK_NEXT       = "breaknext\r";
    public final static String P_WRITE_VAR        = "writevar variableName:newValue\r";
    public final static String P_EXPSENDRX        = "expsendrx Device:Register:sendLength:receiveLength:## ## ... ##\r"; //---

    /* TODO:----- Rover mode ----- */

    public final static String R_ROVER_START  = "roverstart\r";
    public final static String R_ROVER_STOP   = "roverstop\r";
    public final static String R_ROVER_STAT   = "roverstat\r";
    public final static String R_STEP_IN      = "stepin\r";
    public final static String R_STEP_OVER    = "stepover\r";
    public final static String R_STEP_OUT     = "stepout\r";
    public final static String R_SPECTRO      = "spectro\r";

    /* Bluetooth LED cmd */
    public final static String K_BT_LED_ON     = "btledon\r";
    public final static String K_BT_LED_OFF    = "btledoff\r";

    public final static String L_SET_LEFT_MTR  = "setleftmtr ##\r";
    public final static String L_SET_RIGHT_NTR = "setritemtr ##\r";

    /* Servo cmd */
    public final static String SERVO_ON         = "servoon\r";
    public final static String SERVO_OFF        = "servooff\r";
    public final static String SERVO_CENTER     = "servocntr\r";
    public final static String SERVO_CLOSE      = "servoclose\r";
    public final static String SERVO_OPEN       = "servoopen\r";
    public final static String SERVO_UP         = "servoup\r";
    public final static String SERVO_DOWN       = "servodown\r";

    /* Grip cmd */
    public final static String N_GRIP_SET         = "gripset ##\r";
    public final static String N_ELE_SET          = "eleset ##\r";

    public final static String O_CAM_RES          = "camres #\r";

    /* EXP cmd */
    public final static String Q_EXP1_PWR_ON  = "exp1pwron\r";
    public final static String Q_EXP1_PWR_OFF = "exp1pwroff\r";
    public final static String Q_EXP2_PWR_ON  = "exp2pwron\r";
    public final static String Q_EXP2_PWR_OFF = "exp2pwroff\r";
    public final static String Q_EXP1_GET_PWR = "exp1getpwr\r";
    public final static String Q_EXP2_GET_PWR = "exp2getpwr\r";

}
