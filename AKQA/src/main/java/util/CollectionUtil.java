package util;

import model.Booking;
import model.Schedule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-18
 * Time: 下午6:39
 * To change this template use File | Settings | File Templates.
 */
public class CollectionUtil {
    /**
     * like subtract algorithm,I remove the elements in subtrahend list from minuend list
     *
     * @param minuend
     * @param subtrahend
     * @return
     */
    public static List subtract(List minuend, List subtrahend) {
        return (List) CollectionUtils.subtract(minuend, subtrahend);
    }

    /**
     * get some elements from one list,and these elements are different from each other
     *
     * @param list
     * @return
     */
    public static List getDates(List list) {
        return (List) CollectionUtils.collect(
                list, new Transformer() {
            public Object transform(Object arg0) {
                return ((Booking) arg0).getMeetingDate();
            }
        });
    }

    /**
     * get the elements from one list,which their date is equal with the parameter date
     *
     * @param list
     * @param date
     * @return
     */
    public static List getListBySpecialElement(List list, final String date) {
        return (List) CollectionUtils.select(list,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        return date.equals(((Booking) arg0).getMeetingDate());
                    }
                });
    }

    /**
     * For sorting by an ascend order
     *
     * @return
     */
    private static Comparator generateComparator() {
        return new Comparator() {
            public int compare(Object arg0, Object arg1) {
                if (arg0 instanceof Booking && arg1 instanceof Booking) {
                    return DateUtil.compareDate(((Booking) arg0).getBookingTime(),
                            ((Booking) arg1).getBookingTime(), Constants.SDF_DATE_TIME);
                }
                if (arg0 instanceof Schedule && arg1 instanceof Schedule) {
                    return DateUtil.compareDate(((Schedule) arg0).getMeetingDate(),
                            ((Schedule) arg1).getMeetingDate(), Constants.SDF_DATE);
                }

                return 0;
            }
        };
    }

    /**
     * @param list
     */
    public static void ascendSort(List list) {
        if (list.size() > 0) {
            Collections.sort(list, CollectionUtil.generateComparator());
        }
    }
}
