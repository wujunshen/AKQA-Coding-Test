package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-17
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    /**
     * @param time
     * @return
     */
    private static Date getTime(String time) {
        if (!StringUtil.hasColon(time)) {
            time = StringUtil.addColon(time);
        }
        Date date = null;
        try {
            if (time != null && !time.equals("")) {
                Constants.SDF_WORK_HOUR.setLenient(false);
                date = Constants.SDF_WORK_HOUR.parse(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param compareDate
     * @param comparedDate
     * @param sdf
     * @return
     */
    public static int compareDate(String compareDate, String comparedDate, SimpleDateFormat sdf) {
        try {
            return sdf.parse(compareDate).compareTo(sdf.parse(comparedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * get the meeting end time to compare it with end work hour or other booking's meeting start time
     *
     * @param time
     * @param duration
     * @return
     */
    public static String getMeetingEndTime(String time, String duration) {
        Date meetingEndTime = new Date();
        meetingEndTime.setTime(getTime(time).getTime() +
                Long.valueOf(duration) * Constants.ONE_HOUR_MILLISECOND);

        return Constants.SDF_WORK_HOUR.format(meetingEndTime);
    }

    /**
     * @param meetingEndTime
     * @param nextBeginTime
     * @return
     */
    public static boolean isDelay(String meetingEndTime, String nextBeginTime) {
        return getTime(meetingEndTime).compareTo(getTime(nextBeginTime)) > 0;
    }
}