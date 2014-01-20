package model;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-16
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class Meeting {
    private String bookingEmployeeID;
    private String meetingStartTime;
    private String meetingEndTime;

    public String getBookingEmployeeID() {
        return bookingEmployeeID;
    }

    public void setBookingEmployeeID(String bookingEmployeeID) {
        this.bookingEmployeeID = bookingEmployeeID;
    }

    public String getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }
}
