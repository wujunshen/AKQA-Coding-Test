package model;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-16
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class Booking {
    private String bookingTime;
    private String bookingEmployeeID;
    private String meetingDate;
    private String meetingTime;
    private String meetingEndTime;
    private String duration;

    public String getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBookingEmployeeID() {
        return bookingEmployeeID;
    }

    public void setBookingEmployeeID(String bookingEmployeeID) {
        this.bookingEmployeeID = bookingEmployeeID;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
