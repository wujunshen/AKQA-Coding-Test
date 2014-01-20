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
public class StringUtilTest {
    private static String time;

    @Before
    public void init() {
        time = "0900";
    }

    /**
     * test whether a string has colon
     */
    @Test
    public void testHasColon() {
        Assert.assertEquals(false, StringUtil.hasColon(time));
    }

    /**
     * test whether it can add a colon to a string
     */
    @Test
    public void testAddColon() {
        Assert.assertEquals("09:00", StringUtil.addColon(time));
    }

}
