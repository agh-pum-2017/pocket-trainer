package pl.edu.agh.pockettrainer.ui.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

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
        refreshView();
    }

    @Override
    protected void onSelectSettings() {
        // do nothing
    }

    public void onSetDefaultsClick(View view) {
        state.appConfig.setDefaults();
        refreshView();
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

    private TextWatcher onChangeRepoUrl() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                state.appConfig.setRepositoryUrl(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        };
    }

    private void refreshView() {
        final CheckBox voiceCheckbox = template.findViewById(R.id.settings_voice);
        voiceCheckbox.setChecked(state.appConfig.isVoiceEnabled());
        voiceCheckbox.setOnCheckedChangeListener(onVoiceCheck());

        final CheckBox soundCheckbox = template.findViewById(R.id.settings_sound);
        soundCheckbox.setChecked(state.appConfig.isSoundEnabled());
        soundCheckbox.setOnCheckedChangeListener(onSoundCheck());

        final CheckBox vibrateCheckbox = template.findViewById(R.id.settings_vibrate);
        vibrateCheckbox.setChecked(state.appConfig.isVibrateEnabled());
        vibrateCheckbox.setOnCheckedChangeListener(onVibrateCheck());

        final EditText repositoryUrl = template.findViewById(R.id.settings_repository);
        repositoryUrl.setText(state.appConfig.getRepositoryUrl());
        repositoryUrl.addTextChangedListener(onChangeRepoUrl());
    }
}
