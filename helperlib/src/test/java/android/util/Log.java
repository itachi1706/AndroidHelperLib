package android.util;

public class Log {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;

    public static int d(String tag, String msg) {
        System.out.println("DEBUG: " + tag + ": " + msg);
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        System.out.println("DEBUG: " + tag + ": " + msg + " - " + tr);
        return 0;
    }

    public static int i(String tag, String msg) {
        System.out.println("INFO: " + tag + ": " + msg);
        return 0;
    }

    public static int w(String tag, String msg) {
        System.out.println("WARN: " + tag + ": " + msg);
        return 0;
    }

    public static int w(String tag, Throwable tr) {
        System.out.println("WARN: " + tag + ": " + tr);
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        System.out.println("WARN: " + tag + ": " + msg + " - " + tr);
        return 0;
    }

    public static int e(String tag, String msg) {
        System.out.println("ERROR: " + tag + ": " + msg);
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        System.out.println("ERROR: " + tag + ": " + msg + " - " + tr);
        return 0;
    }

    public static int wtf(String tag, String msg) {
        System.out.println("WTF: " + tag + ": " + msg);
        return 0;
    }

    public static int println(int priority, String tag, String msg) {
        System.out.println(priority + ": " + tag + ": " + msg);
        return 0;
    }

    // add other methods if required...
}
