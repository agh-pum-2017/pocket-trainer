package pl.edu.agh.pockettrainer;

import android.content.Context;
import android.content.SharedPreferences;

import pl.edu.agh.pockettrainer.program.Logger;

public class AppConfig {

    private static final String NAME = "pl.edu.agh.pockettrainer";
    private static final String KEY_FIRST_RUN = "firstRun";
    private static final String KEY_ACTIVE_PROGRAM_ID = "programId";

    private final Logger logger = new Logger(AppConfig.class);
    private final SharedPreferences preferences;

    public AppConfig(Context context) {
        this.preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        if (preferences.getBoolean(KEY_FIRST_RUN, true)) {
            logger.debug("Application is started for the first time");
            preferences.edit().putBoolean(KEY_FIRST_RUN, false).apply();
            return true;
        } else {
            return false;
        }
    }

    public String getActiveProgramId() {
        return preferences.getString(KEY_ACTIVE_PROGRAM_ID, null);
    }

    public void setActiveProgramId(String value) {
        preferences.edit().putString(KEY_ACTIVE_PROGRAM_ID, value).apply();
    }

    public void unsetActiveProgramId() {
        preferences.edit().remove(KEY_ACTIVE_PROGRAM_ID).apply();
    }

    public int getCountdownIntervalSeconds() {
        return 3; // TODO
    }
}
