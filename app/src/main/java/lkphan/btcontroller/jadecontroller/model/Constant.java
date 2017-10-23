package lkphan.btcontroller.jadecontroller.model;

/**
 * Created by kimiboo on 2017-10-11.
 */

public class Constant {

    public static final  String SOI            = "0xff 0xd8 0xff";
    public static final  String EOI            = "0xff 0xd9";
    public static final  int COLON             = 58;
    public static final  String ROVER_IMG      = "jpeg:";
    public static final  String ROVER_STAT     = "stat:";
    public static final  String ROVER_END      = "term:";
    public static final  String ROVER_BATT     = "batt:";
    public static final  String ROVER_ACTIVE         = "\rRover Active\r"; //"\rCamera Active\r\r\r\rRover Active\r";
    public static final  String ROVER_CAM_ACTIVE     = "\rCamera Active\r";
//    “0” – 160x120, “1” – 320x240, “2” – 640x480
    public static final  int IMG_WIDTH            = 160;
    public static final  int IMG_HEIGHT           = 120;
    public static final  int IMG_SIZE0           = 76800; //res 0
    public static final  int IMG_SIZE1           = 307200; //res 1
    public static final  int IMG_SIZE2           = 1228800; //res 2

}
