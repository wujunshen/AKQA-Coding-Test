package action;

import util.Constants;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Frank Woo(吴峻申)
 * Date: 14-1-16
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public class AKQARun {
    public static String JAR_PATH;

    /**
     * set path of jar file
     *
     * @param clazz
     */
    public AKQARun(Class clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, Constants.ENCODING);
            java.io.File parent = new File(path).getParentFile();
            if (parent != null) {
                this.JAR_PATH = java.net.URLDecoder.decode(parent.getAbsolutePath(), Constants.ENCODING)
                        + Constants.SLASH;
            }
        } catch (java.io.UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * You can see detail comments in Handle.java and other files in util folder
     *
     * @param args args[0] is input file name,args[1] is output file name
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        AKQARun akqaRun = new AKQARun(AKQARun.class);
        Handle handle = new Handle();
        String inputFileName = args[0] + Constants.FILE_SUFFIX;
        String outputFileName = args[1] + Constants.FILE_SUFFIX;

        try {
            handle.writeOutputFile(new File(JAR_PATH + outputFileName),
                    handle.getSchedules(handle.getDataFromInputFile(
                            new File(JAR_PATH + inputFileName))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
