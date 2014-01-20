package util;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-17
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public class DateUtilTest {
    private static String time;
    private static String duration;
    private static String meetingEndTime;
    private static String endOfficeHour;
    private static String nextBeginTime;
    private static String compareDateTime;
    private static String comparedDateTime;
    private static String compareDate;
    private static String comparedDate;

    /**
     * init the test data
     */
    @Before
    public void init() {
        time = "9:00";
        duration = "2";

        meetingEndTime = "16:00";
        endOfficeHour = "17:30";
        nextBeginTime = "15:00";

        compareDateTime = "2011-03-16 09:28:23";
        comparedDateTime = "2011-03-17 11:23:45";

        compareDate = "2011-03-16";
        comparedDate = "2011-03-13";
    }

    /**
     * test whether it can calculate end time
     */
    @Test
    public void testGetMeetingEndTime() {
        Assert.assertEquals("11:00", DateUtil.getMeetingEndTime(time, duration));
    }

    /**
     * test whether a time is posterior to another time
     */
    @Test
    public void testIsDelay() {
        Assert.assertEquals(false, DateUtil.isDelay(meetingEndTime, endOfficeHour));
        Assert.assertEquals(true, DateUtil.isDelay(meetingEndTime, nextBeginTime));
    }

    /**
     * test whether it can compare two dates
     */
    @Test
    public void testCompareDate() {
        Assert.assertEquals(-1, DateUtil.compareDate(compareDateTime, comparedDateTime, Constants.SDF_DATE_TIME));
        Assert.assertEquals(1, DateUtil.compareDate(compareDate, comparedDate, Constants.SDF_DATE));
    }

}
