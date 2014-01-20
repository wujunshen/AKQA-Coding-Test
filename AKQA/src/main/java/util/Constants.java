package util;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-18
 * Time: 下午6:10
 * To change this template use File | Settings | File Templates.
 */
public class Constants {
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SLASH = "\\";
    public static final String ENCODING = "UTF-8";
    public static final String FILE_SUFFIX = ".csv";
    public static final String START_OFFICE_HOUR = "start_office_hour";
    public static final String END_OFFICE_HOUR = "end_office_hour";
    public static final String BOOKING = "booking_info";
    public static final long ONE_HOUR_MILLISECOND = 60 * 60 * 1000;
    public static final SimpleDateFormat SDF_WORK_HOUR = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final int SUCCESS_CODE = 1;
    public static final int FAIL_CODE = 0;
}
