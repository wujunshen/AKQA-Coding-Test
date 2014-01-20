package action;

import model.Booking;
import model.Meeting;
import model.Schedule;
import util.CollectionUtil;
import util.Constants;
import util.DateUtil;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-16
 * Time: 下午3:52
 * To change this template use File | Settings | File Templates.
 */
public class Handle {
    /**
     * copy Booking object to Meeting object
     *
     * @param booking
     * @return
     */
    private Meeting copyBooking2Meeting(Booking booking) {
        Meeting meeting = new Meeting();
        meeting.setMeetingStartTime(booking.getMeetingTime());
        meeting.setMeetingEndTime(booking.getMeetingEndTime());
        meeting.setBookingEmployeeID(booking.getBookingEmployeeID());
        return meeting;
    }

    /**
     * When I write result to the output file every time，I clear the content of last time first
     *
     * @param file
     * @throws IOException
     */
    private void clearFileContent(File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write("");
        fw.close();
    }

    /**
     * extract the content of the input file to a Map object
     * make the booking info to be a list
     * the element is Booking object
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Map getDataFromInputFile(File file) throws IOException {
        List<Booking> inputDataList = new ArrayList();
        Map inputDataMap = new HashMap();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            String[] strings = line.split(Constants.COMMA);

            if (strings.length == 2) {
                inputDataMap.put(Constants.START_OFFICE_HOUR, strings[0]);
                inputDataMap.put(Constants.END_OFFICE_HOUR, strings[1]);
            } else {
                Booking booking = new Booking();
                booking.setBookingTime(strings[0]);
                booking.setBookingEmployeeID(strings[1]);
                booking.setMeetingDate(strings[2]);
                booking.setMeetingTime(strings[3]);
                booking.setDuration(strings[4]);
                inputDataList.add(booking);
            }
        }
        br.close();

        inputDataMap.put(Constants.BOOKING, inputDataList);
        return inputDataMap;
    }

    /**
     * write the list including Schedule object to the output file
     *
     * @param file
     * @param schedules
     * @return
     * @throws IOException
     */
    public int writeOutputFile(File file, List<Schedule> schedules) throws Exception {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            clearFileContent(file);

            for (Schedule schedule : schedules) {
                bw.newLine();
                bw.write(schedule.getMeetingDate());

                List<Meeting> meetings = schedule.getMeeting();
                for (Meeting meeting : meetings) {
                    bw.newLine();
                    bw.write(meeting.getMeetingStartTime() + Constants.COMMA
                            + meeting.getMeetingEndTime() + Constants.COMMA
                            + meeting.getBookingEmployeeID());
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.FAIL_CODE;
        }
        return Constants.SUCCESS_CODE;
    }

    /**
     * filtrate the list including Booking object
     * if one booking object's meeting end time has been delayed to the end office hour
     * I remove this object from the list
     *
     * @param bookings
     * @param endOfficeHour
     * @return
     */
    public List<Booking> filtrateOutsideOfficeHour(List<Booking> bookings,
                                                   String endOfficeHour) {
        List<Booking> outsideOfficeHourBookings = new ArrayList();

        for (Booking booking : bookings) {
            String meetingEndTime = DateUtil.getMeetingEndTime(booking.getMeetingTime(),
                    booking.getDuration());

            if (DateUtil.isDelay(meetingEndTime, endOfficeHour))
                outsideOfficeHourBookings.add(booking);
            else booking.setMeetingEndTime(meetingEndTime);
        }

        return CollectionUtil.subtract(bookings, outsideOfficeHourBookings);
    }


    /**
     * get a list including meeting date
     * the date is unique
     * It means if the booking include 2 days meeting schedule
     * the list will include these 2 days info
     *
     * @param bookings
     * @return
     */
    public Object[] getUniqueMeetingDate(List<Booking> bookings) {
        Set<String> uniqueMeetingDates = new HashSet<String>();
        uniqueMeetingDates.addAll(CollectionUtil.getDates(bookings));
        return uniqueMeetingDates.toArray();
    }

    public List<Booking> filtrateOverlapBookingInfo(
            List<Booking> bookingList) {
        List<Booking> overlapBookings = new ArrayList();

        for (int i = 0; i < bookingList.size() - 1; i++) {
            String meetingEndTime = (bookingList.get(i)).getMeetingEndTime();
            for (int j = i + 1; j < bookingList.size(); j++) {
                Booking booking = bookingList.get(j);
                if (DateUtil.isDelay(meetingEndTime, booking.getMeetingTime())) {
                    overlapBookings.add(booking);
                }
            }
        }

        return CollectionUtil.subtract(bookingList, overlapBookings);
    }

    /**
     * get the result object(Schedule object)
     * then I can write that to the output file
     *
     * @param data
     * @return
     */
    public List<Schedule> getSchedules(Map data) {
        List<Schedule> schedules = new ArrayList();
        List<Booking> bookings = (ArrayList) data.get(Constants.BOOKING);

        // remove the booking which the meeting end time is delayed to the outside office hour
        bookings = filtrateOutsideOfficeHour(bookings,
                (String) data.get(Constants.END_OFFICE_HOUR));

        //get unique meeting date in booking list
        for (Object uniqueMeetingDate : getUniqueMeetingDate(bookings)) {
            Schedule schedule = new Schedule();
            schedule.setMeetingDate((String) uniqueMeetingDate);

            //get booking objects which their meeting date is a same day
            List<Booking> bookingsOfUniqueMeetingDate = CollectionUtil.getListBySpecialElement
                    (bookings, (String) uniqueMeetingDate);

            //I sort these objects according an ascend order
            CollectionUtil.ascendSort(bookingsOfUniqueMeetingDate);

            //filtrate some booking objects which their meeting end time is delayed to any others' meeting start time
            bookingsOfUniqueMeetingDate = filtrateOverlapBookingInfo(bookingsOfUniqueMeetingDate);

            // compile the remain booking objects to meeting objects and schedule object
            List<Meeting> meetings = new ArrayList();
            for (Booking booking : bookingsOfUniqueMeetingDate) {
                meetings.add(copyBooking2Meeting(booking));
            }

            schedule.setMeeting(meetings);
            schedules.add(schedule);
        }

        //schedule objects may be not follow ascend order,so I sort them as I sort booking objects
        CollectionUtil.ascendSort(schedules);
        return schedules;
    }
}