package pl.edu.agh.pockettrainer;

import android.content.Context;
import android.content.SharedPreferences;

import pl.edu.agh.pockettrainer.program.Logger;

public class AppConfig {

    private static final String NAME = "pl.edu.agh.pockettrainer";

    private static final String KEY_FIRST_RUN = "firstRun";
    private static final String KEY_ACTIVE_PROGRAM_ID = "programId";
    private static final String KEY_VOICE_INSTRUCTIONS = "voice";
    private static final String KEY_SOUND_AND_MUSIC = "sound";
    private static final String KEY_VIBRATE = "vibrate";

    private static final boolean DEFAULT_VOICE_ENABLED = true;
    private static final boolean DEFAULT_SOUND_ENABLED = true;
    private static final boolean DEFAULT_VIBRATE_ENABLED = true;

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

    public boolean isVoiceEnabled() {
        return preferences.getBoolean(KEY_VOICE_INSTRUCTIONS, DEFAULT_VOICE_ENABLED);
    }

    public void setVoiceEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_VOICE_INSTRUCTIONS, enabled).apply();
    }

    public boolean isSoundEnabled() {
        return preferences.getBoolean(KEY_SOUND_AND_MUSIC, DEFAULT_SOUND_ENABLED);
    }

    public void setSoundEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_SOUND_AND_MUSIC, enabled).apply();
    }

    public boolean isVibrateEnabled() {
        return preferences.getBoolean(KEY_VIBRATE, DEFAULT_VIBRATE_ENABLED);
    }

    public void setVibrateEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_VIBRATE, enabled).apply();
    }
}
