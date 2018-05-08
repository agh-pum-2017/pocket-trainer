package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class SettingsActivity extends WithMenuActivity {

    private ApplicationState state;

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_settings;
    }

    @Override
    protected String getTitleForActivity() {
        return "Settings";
    }

    @Override
    protected void initView(View child) {

        state = (ApplicationState) getApplicationContext();

        final CheckBox voiceCheckbox = template.findViewById(R.id.settings_voice);
        voiceCheckbox.setChecked(state.appConfig.isVoiceEnabled());
        voiceCheckbox.setOnCheckedChangeListener(onVoiceCheck());

        final CheckBox soundCheckbox = template.findViewById(R.id.settings_sound);
        soundCheckbox.setChecked(state.appConfig.isSoundEnabled());
        soundCheckbox.setOnCheckedChangeListener(onSoundCheck());

        final CheckBox vibrateCheckbox = template.findViewById(R.id.settings_vibrate);
        vibrateCheckbox.setChecked(state.appConfig.isVibrateEnabled());
        vibrateCheckbox.setOnCheckedChangeListener(onVibrateCheck());
    }

    @Override
    protected void onSelectSettings() {
        // do nothing
    }

    private CompoundButton.OnCheckedChangeListener onVoiceCheck() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                state.appConfig.setVoiceEnabled(isChecked);
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener onSoundCheck() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                state.appConfig.setSoundEnabled(isChecked);
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener onVibrateCheck() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                state.appConfig.setVibrateEnabled(isChecked);
            }
        };
    }
}
