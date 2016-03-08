package capstone.boardgame.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Kyle on 2/26/2016.
 */
public class Log {
    private static Calendar cal = Calendar.getInstance();
    private static DateFormat df = DateFormat.getDateInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    public static void d(String tag, String message) {
        cal = Calendar.getInstance();
        System.out.printf("|" + sdf.format(cal.getTime()) + " [" + tag + "]| " + message + "\n");
    }
}
