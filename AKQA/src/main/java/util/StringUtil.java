package util;


/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-17
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    /**
     * In input file,the format of the start and end work hour is no colon format
     * So I use it to tell myself whether a string has a colon
     *
     * @param str
     * @return
     */
    public static boolean hasColon(String str) {
        return str.contains(Constants.COLON);
    }

    /**
     * If a string has no colon,I add it
     * Because I can convert the string including colon to a Date object
     *
     * @param time
     * @return
     */
    public static String addColon(String time) {
        return time.substring(0, time.length() - 2) +
                Constants.COLON + time.substring(time.length() - 2, time.length());
    }
}