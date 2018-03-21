package pl.edu.agh.pockettrainer;

import android.content.Context;
import android.content.SharedPreferences;

public class App {

    private static final String NAME = "pl.edu.agh.pockettrainer";
    private static final String KEY_FIRST_RUN = "firstRun";
    private static final String KEY_ACTIVE_PROGRAM_ID = "activeProgramId";

    private final SharedPreferences sharedPreferences;

    public App(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        if (sharedPreferences.getBoolean(KEY_FIRST_RUN, true)) {
            sharedPreferences.edit().putBoolean(KEY_FIRST_RUN, false).apply();
            return true;
        } else {
            return false;
        }
    }

    public boolean hasActiveProgram() {
        return null != getActiveProgramId();
    }

    public String getActiveProgramId() {
        return sharedPreferences.getString(KEY_ACTIVE_PROGRAM_ID, null);
    }

    public void setActiveProgramId(String id) {
        sharedPreferences.edit().putString(KEY_ACTIVE_PROGRAM_ID, id).apply();
    }
}
