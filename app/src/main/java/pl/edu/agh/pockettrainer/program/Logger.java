package pl.edu.agh.pockettrainer.program;

import android.util.Log;

public class Logger {

    private final String tag;

    public Logger(String tag) {
        this.tag = tag;
    }

    public Logger(Class<?> classObject) {
        this.tag = classObject.getCanonicalName();
    }

    public void verbose(String message, Object... args) {
        Log.v(tag, String.format(message, args));
    }

    public void debug(String message, Object... args) {
        Log.d(tag, String.format(message, args));
    }

    public void info(String message, Object... args) {
        Log.i(tag, String.format(message, args));
    }

    public void warning(String message, Object... args) {
        Log.w(tag, String.format(message, args));
    }

    public void error(String message, Object... args) {
        Log.e(tag, String.format(message, args));
    }

    public void error(Throwable throwable, String message, Object... args) {
        Log.e(tag, String.format(message, args), throwable);
    }
}
