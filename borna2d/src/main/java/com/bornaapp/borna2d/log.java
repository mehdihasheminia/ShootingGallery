package com.bornaapp.borna2d;

import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Hashemi on 9/1/2016.
 * <p/>
 * We need unique and global access to the functions of this class
 * but no need to instantiate, inherit, pass as argument, etc....
 * So, as this class is merely a bunch of functions, we used
 * "Static" data structure instead of "singlton object".
 */

public class log {

    private enum LogLevel {
        LOG_NONE,     // Application.LOG_NONE=0.   mutes all logging.
        LOG_ERROR,    // Application.LOG_ERROR=1.  logs only error messages.
        LOG_INFO,     // Application.LOG_INFO=2.   logs error & info messages(non debug messages).
        LOG_DEBUG     // Application.LOG_DEBUG=3.  logs all messages.
    }

    private static final char BACKSLASH = (char) 27;
    // ANSI scape codes
    private static final String ANSI_RESET = "[0m";
    private static final String ANSI_BLACK = "0";
    private static final String ANSI_RED = "1";
    private static final String ANSI_GREEN = "2";
    private static final String ANSI_YELLOW = "3";
    private static final String ANSI_BLUE = "4";
    private static final String ANSI_PURPLE = "5";
    private static final String ANSI_CYAN = "6";
    private static final String ANSI_WHITE = "7";

    private static void text(String textToPrint, String foreColor) {
        String ansiFormat = "[3" + foreColor + "m";
        System.out.println(BACKSLASH + ansiFormat + textToPrint + BACKSLASH + ANSI_RESET);
    }

    private static void text(String textToPrint, String foreColor, String backColor) {
        String ansiFormat = "[3" + foreColor + ";4" + backColor + "m";
        System.out.println(BACKSLASH + ansiFormat + textToPrint + BACKSLASH + ANSI_RESET);
    }

    private static LogLevel getEngineLogLevel() {
        try {
            switch (Engine.getInstance().getConfig().logLevel) {
                case 1:
                    return LogLevel.LOG_ERROR;
                case 2:
                    return LogLevel.LOG_INFO;
                case 3:
                    return LogLevel.LOG_DEBUG;
                case 0:
                default:
                    return LogLevel.LOG_NONE;
            }
        } catch (Exception e) {
            return LogLevel.LOG_NONE;
        }
    }

    private static String getCurrentLevelName() {
        try {
            return Engine.getInstance().getCurrentLevel().getName();
        } catch (Exception e) {
            return "UnknownLevel";
        }
    }

    private static String getCurrentMethodName() {
        try {
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            StackTraceElement e = stacktrace[3];
            //extract class name
            String[] classNameArray = e.getClassName().split("\\.", 0);
            String className = classNameArray[classNameArray.length - 1];
            //extract method name
            String methodName = e.getMethodName();

            return className + "." + methodName + "()";
        } catch (Exception e) {
            return "UnknownMethod";
        }
    }

    public static void error(String message) {
        if (getEngineLogLevel() == LogLevel.LOG_NONE)
            return;
        text(getCurrentLevelName() + "; " + getCurrentMethodName() + ": " + message, ANSI_RED);
    }

    public static void error(boolean value) {
        error(Boolean.toString(value));
    }

    public static void error(float value) {
        error(Float.toString(value));
    }

    public static void error(double value) {
        error(Double.toString(value));
    }

    public static void error(int value) {
        error(Integer.toString(value));
    }

    public static void error(Vector2 value) {
        error("[" + Float.toString(value.x) + ", " + Float.toString(value.y) + "]");
    }

    public static void info(String message) {
        if (getEngineLogLevel() == LogLevel.LOG_ERROR || getEngineLogLevel() == LogLevel.LOG_NONE)
            return;
        text(getCurrentLevelName() + "; " + getCurrentMethodName() + ": " + message, ANSI_BLUE);
    }

    public static void info(boolean value) {
        info(Boolean.toString(value));
    }

    public static void info(float value) {
        info(Float.toString(value));
    }

    public static void info(double value) {
        info(Double.toString(value));
    }

    public static void info(int value) {
        info(Integer.toString(value));
    }

    public static void info(Vector2 value) {
        info("[" + Float.toString(value.x) + ", " + Float.toString(value.y) + "]");
    }

    public static void debug(String message) {
        if (getEngineLogLevel() == LogLevel.LOG_ERROR || getEngineLogLevel() == LogLevel.LOG_INFO || getEngineLogLevel() == LogLevel.LOG_NONE)
            return;
        text(getCurrentLevelName() + "; " + getCurrentMethodName() + ": " + message, ANSI_BLACK);
    }

    public static void debug(boolean value) {
        debug(Boolean.toString(value));
    }

    public static void debug(float value) {
        debug(Float.toString(value));
    }

    public static void debug(double value) {
        debug(Double.toString(value));
    }

    public static void debug(int value) {
        debug(Integer.toString(value));
    }

    public static void debug(Vector2 value) {
        debug("[" + Float.toString(value.x) + ", " + Float.toString(value.y) + "]");
    }
}