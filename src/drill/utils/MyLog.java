package drill.utils;

public class MyLog {

    public static String TAG = "MyLog";
    private static boolean LOG_ON = true;
    private static final int REAL_METHOD_POS = 2;
    private static StackTraceElement[] ste;
    private static StackTraceElement realMethod;

    //기본 출력 내용
    private static String prefix() {
        ste = new Throwable().getStackTrace();
        realMethod = ste[REAL_METHOD_POS];
        return realMethod.getFileName()+"."+realMethod.getMethodName()
                + "("+realMethod.getLineNumber() +")"+" :: "/*+ "()-" + "[Thread:" + thread + "] "*/;
    }

    //호출자 정보 출력
    private static void printCallerLink(String tag) {
        try {
            StackTraceElement[] stack = new Throwable().getStackTrace();
            StackTraceElement caller = stack[3];
            System.out.println("caller from :: " + caller);
        } catch (ArrayIndexOutOfBoundsException e)  {
            System.out.println("ArrayIndexOutOfBoundsException");
        }
    }

    private static int UI_HASH_CODE = 0;
    public static void setUiThreadHashCode (int uiHashCode) {
        UI_HASH_CODE = uiHashCode;
    }

    public static void setLogOn(boolean enable){
        LOG_ON = enable;
    }

    public static boolean getLogOn(){
        return LOG_ON;
    }

    public static void d(String tag, String msg) {
        if (LOG_ON) {
            System.out.println(prefix() + msg + " at " + realMethod);
            printCallerLink(tag);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_ON) {
            System.out.println(prefix()+msg);
        }
    }

    //파라메터 2개(TAG 를 지정 방식)
    public static void i(String tag, String msg) {
        if (LOG_ON) {
            System.out.println(prefix() + msg + " at " + realMethod);
            printCallerLink(tag);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_ON) {
            System.out.println(prefix() + msg + " at " + realMethod);
            printCallerLink(tag);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_ON) {
            System.out.println(prefix() + msg + " at " + realMethod);
            printCallerLink(tag);
        }
    }

    //파라메터 1개(TAG 통일 방식)
    public static void i(String msg) {
        if (LOG_ON) {
            System.out.println(prefix() + msg + " at " + realMethod);
//            printCallerLink(TAG);
        }
    }

    public static void w(String msg) {
        if (LOG_ON) {
            System.out.println(prefix() + msg + " at " + realMethod);
            printCallerLink(TAG);
        }
    }

    public static void e(String msg) {
        if (LOG_ON) {
            System.out.println(prefix() + msg + " at " + realMethod);
            printCallerLink(TAG);
        }
    }


}
