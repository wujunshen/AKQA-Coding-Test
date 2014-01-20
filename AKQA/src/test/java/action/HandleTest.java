package action;

import junit.framework.Assert;
import model.Booking;
import model.Meeting;
import model.Schedule;
import org.junit.Before;
import org.junit.Test;
import util.CollectionUtil;
import util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-16
 * Time: 下午4:25
 * To change this template use File | Settings | File Templates.
 */
public class HandleTest {
    private static String INPUT_FILE_NAME;
    private static String OUTPUT_FILE_NAME;
    private static AKQARun akqaRun;
    private static Handle handle = new Handle();
    private static List<Schedule> outputData = new ArrayList();
    private static Map inputData = new HashMap();
    private static List<Booking> bookings = new ArrayList<Booking>();
    private static String meetingDate;

    /**
     * init the test data
     */
    @Before
    public void init() {
        akqaRun = new AKQARun(AKQARun.class);
        handle = new Handle();
        outputData = initOutputTestData();
        inputData = initInputTestData();
        INPUT_FILE_NAME = "input" + Constants.FILE_SUFFIX;
        OUTPUT_FILE_NAME = "expected" + Constants.FILE_SUFFIX;
        bookings = (ArrayList) inputData.get(Constants.BOOKING);
        meetingDate = "2011-03-21";
    }

    /**
     * test whether it can correctly extract data from the input file
     *
     * @throws Exception
     */
    @Test
    public void testGetDataFromInputFile() throws Exception {
        try {
            Map actualInputData = handle.getDataFromInputFile(new File(AKQARun.JAR_PATH +
                    INPUT_FILE_NAME));
            Assert.assertEquals(inputData.get(Constants.START_OFFICE_HOUR),
                    actualInputData.get(Constants.START_OFFICE_HOUR));
            Assert.assertEquals(inputData.get(Constants.END_OFFICE_HOUR),
                    actualInputData.get(Constants.END_OFFICE_HOUR));

            List<Booking> actualBookings =
                    (ArrayList) actualInputData.get(Constants.BOOKING);
            List<Booking> expectedBookings =
                    (ArrayList) inputData.get(Constants.BOOKING);
            for (int i = 0; i < actualBookings.size(); i++) {
                Booking actualBooking = actualBookings.get(i);
                Booking expectedBooking = expectedBookings.get(i);

                Assert.assertEquals(expectedBooking.getBookingTime(),
                        actualBooking.getBookingTime());
                Assert.assertEquals(expectedBooking.getBookingEmployeeID(),
                        actualBooking.getBookingEmployeeID());
                Assert.assertEquals(expectedBooking.getMeetingDate(),
                        actualBooking.getMeetingDate());
                Assert.assertEquals(expectedBooking.getMeetingTime(),
                        actualBooking.getMeetingTime());
                Assert.assertEquals(expectedBooking.getDuration(),
                        actualBooking.getDuration());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * test whether it can correctly write result data to the output file
     *
     * @throws Exception
     */
    @Test
    public void testWriteOutputFile() throws Exception {
        try {
            List<Schedule> actualOutputData = handle.getSchedules(handle.getDataFromInputFile(
                    new File(AKQARun.JAR_PATH + INPUT_FILE_NAME)));

            Assert.assertEquals(Constants.SUCCESS_CODE, handle.writeOutputFile(
                    new File(AKQARun.JAR_PATH + OUTPUT_FILE_NAME), actualOutputData));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * test whether it can filtrate the outside office hour’ booking
     *
     * @throws IOException
     */
    @Test
    public void testFiltrateOutsideOfficeHour() throws IOException {
        Assert.assertEquals(5, bookings.size());

        bookings = handle.filtrateOutsideOfficeHour(bookings,
                (String) inputData.get(Constants.END_OFFICE_HOUR));

        Assert.assertEquals(4, bookings.size());
    }

    /**
     * test whether it can get unique meeting date
     *
     * @throws IOException
     */
    @Test
    public void testGetUniqueMeetingDate() throws IOException {
        bookings = handle.filtrateOutsideOfficeHour(bookings,
                (String) inputData.get(Constants.END_OFFICE_HOUR));
        Object[] objects = handle.getUniqueMeetingDate(bookings);

        Assert.assertEquals(2, objects.length);
        Assert.assertEquals("2011-03-22", objects[0]);
        Assert.assertEquals("2011-03-21", objects[1]);
    }

    /**
     * test whether it can filtrate booking which the time is overlap
     *
     * @throws IOException
     */
    @Test
    public void testFiltrateOverlapBookings() throws IOException {
        bookings = handle.filtrateOutsideOfficeHour(bookings,
                (String) inputData.get(Constants.END_OFFICE_HOUR));

        List<Booking> bookingsOfUniqueMeetingTime = CollectionUtil.getListBySpecialElement
                (bookings, meetingDate);
        CollectionUtil.ascendSort(bookingsOfUniqueMeetingTime);
        bookingsOfUniqueMeetingTime = handle.filtrateOverlapBookingInfo(bookingsOfUniqueMeetingTime);

        for (Booking booking : bookingsOfUniqueMeetingTime) {
            Assert.assertEquals("2011-03-16 12:34:56", booking.getBookingTime());
            Assert.assertEquals("EMP002", booking.getBookingEmployeeID());
            Assert.assertEquals("2011-03-21", booking.getMeetingDate());
            Assert.assertEquals("09:00", booking.getMeetingTime());
            Assert.assertEquals("11:00", booking.getMeetingEndTime());
            Assert.assertEquals("2", booking.getDuration());
        }
    }

    /**
     * test whether it can get schedules
     *
     * @throws IOException
     */
    @Test
    public void testGetSchedules() throws IOException {
        List<Schedule> actualSchedules = handle.getSchedules(inputData);
        for (int i = 0; i < actualSchedules.size(); i++) {
            Schedule actualSchedule = actualSchedules.get(i);
            Schedule expectedSchedule = outputData.get(i);

            Assert.assertEquals(expectedSchedule.getMeetingDate(),
                    actualSchedule.getMeetingDate());

            List<Meeting> actualMeetings = actualSchedule.getMeeting();
            List<Meeting> expectedMeetings = expectedSchedule.getMeeting();
            for (int j = 0; j < actualMeetings.size(); j++) {
                Meeting actualMeeting = actualMeetings.get(j);
                Meeting expectedMeeting = expectedMeetings.get(j);
                Assert.assertEquals(expectedMeeting.getBookingEmployeeID(),
                        actualMeeting.getBookingEmployeeID());
                Assert.assertEquals(expectedMeeting.getMeetingStartTime(),
                        actualMeeting.getMeetingStartTime());
                Assert.assertEquals(expectedMeeting.getMeetingEndTime(),
                        actualMeeting.getMeetingEndTime());
            }
        }
    }

    /**
     * init test output data
     *
     * @return
     */
    private List<Schedule> initOutputTestData() {
        List<Schedule> outputData = new ArrayList();
        Schedule scheduleOfFirstDay = new Schedule();
        scheduleOfFirstDay.setMeetingDate("2011-03-21");
        List<Meeting> meetingInfosOfFirstDay = new ArrayList();
        Meeting meetingOfEMP002 = new Meeting();
        meetingOfEMP002.setMeetingStartTime("09:00");
        meetingOfEMP002.setMeetingEndTime("11:00");
        meetingOfEMP002.setBookingEmployeeID("EMP002");
        meetingInfosOfFirstDay.add(meetingOfEMP002);
        scheduleOfFirstDay.setMeeting(meetingInfosOfFirstDay);
        outputData.add(scheduleOfFirstDay);

        Schedule scheduleOfSecondDay = new Schedule();
        scheduleOfSecondDay.setMeetingDate("2011-03-22");
        List<Meeting> meetingInfosOfSecondDay = new ArrayList();
        Meeting meetingOfEMP003 = new Meeting();
        meetingOfEMP003.setMeetingStartTime("14:00");
        meetingOfEMP003.setMeetingEndTime("16:00");
        meetingOfEMP003.setBookingEmployeeID("EMP003");
        meetingInfosOfSecondDay.add(meetingOfEMP003);
        Meeting meetingOfEMP004 = new Meeting();
        meetingOfEMP004.setMeetingStartTime("16:00");
        meetingOfEMP004.setMeetingEndTime("17:00");
        meetingOfEMP004.setBookingEmployeeID("EMP004");
        meetingInfosOfSecondDay.add(meetingOfEMP004);
        scheduleOfSecondDay.setMeeting(meetingInfosOfSecondDay);
        outputData.add(scheduleOfSecondDay);

        return outputData;
    }


    /**
     * init test input data
     *
     * @return
     */
    private Map initInputTestData() {
        Map inputData = new HashMap();
        inputData.put(Constants.START_OFFICE_HOUR, "0900");
        inputData.put(Constants.END_OFFICE_HOUR, "1730");

        List<Booking> bookings = new ArrayList();
        Booking bookingOfEMP001 = new Booking();
        bookingOfEMP001.setBookingTime("2011-03-17 10:17:06");
        bookingOfEMP001.setBookingEmployeeID("EMP001");
        bookingOfEMP001.setMeetingDate("2011-03-21");
        bookingOfEMP001.setMeetingTime("09:00");
        bookingOfEMP001.setDuration("2");

        Booking bookingOfEMP002 = new Booking();
        bookingOfEMP002.setBookingTime("2011-03-16 12:34:56");
        bookingOfEMP002.setBookingEmployeeID("EMP002");
        bookingOfEMP002.setMeetingDate("2011-03-21");
        bookingOfEMP002.setMeetingTime("09:00");
        bookingOfEMP002.setDuration("2");

        Booking bookingOfEMP003 = new Booking();
        bookingOfEMP003.setBookingTime("2011-03-16 09:28:23");
        bookingOfEMP003.setBookingEmployeeID("EMP003");
        bookingOfEMP003.setMeetingDate("2011-03-22");
        bookingOfEMP003.setMeetingTime("14:00");
        bookingOfEMP003.setDuration("2");

        Booking bookingOfEMP004 = new Booking();
        bookingOfEMP004.setBookingTime("2011-03-17 11:23:45");
        bookingOfEMP004.setBookingEmployeeID("EMP004");
        bookingOfEMP004.setMeetingDate("2011-03-22");
        bookingOfEMP004.setMeetingTime("16:00");
        bookingOfEMP004.setDuration("1");

        Booking bookingOfEMP005 = new Booking();
        bookingOfEMP005.setBookingTime("2011-03-15 17:29:12");
        bookingOfEMP005.setBookingEmployeeID("EMP005");
        bookingOfEMP005.setMeetingDate("2011-03-21");
        bookingOfEMP005.setMeetingTime("16:00");
        bookingOfEMP005.setDuration("3");

        bookings.add(bookingOfEMP001);
        bookings.add(bookingOfEMP002);
        bookings.add(bookingOfEMP003);
        bookings.add(bookingOfEMP004);
        bookings.add(bookingOfEMP005);

        inputData.put(Constants.BOOKING, bookings);
        return inputData;
    }
}
