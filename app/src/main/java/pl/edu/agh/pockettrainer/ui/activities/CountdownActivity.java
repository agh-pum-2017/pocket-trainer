package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class CountdownActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = new Logger(CountdownActivity.class);

    private CountDownTimer timer;
    private int count;
    private TextToSpeech tts;

    private TextView title;
    private TextView label;
    private TextView labelHidden;

    @Override
    public void onBackPressed() {
        // prevent from interrupting the countdown
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        tts = new TextToSpeech(this, this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        title = findViewById(R.id.countdown_title);
        label = findViewById(R.id.countdown_label);
        labelHidden = findViewById(R.id.countdown_label_hidden);

        title.setVisibility(View.VISIBLE);
        label.setVisibility(View.INVISIBLE);
        labelHidden.setVisibility(View.INVISIBLE);

        resetTimer();
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        stopTimer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopTimer();
        super.onStop();
    }

    private void resetTimer() {
        final AppConfig appConfig = new AppConfig(this);
        count = appConfig.getCountdownIntervalSeconds();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            final int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                logger.error("TTS language is not supported: US English");
            } else {
                startTimer();
            }
        } else {
            logger.error("Unable to initialize TTS");
        }
    }

    private void navigateToTrainer() {

        final ApplicationState state = (ApplicationState) getApplicationContext();

        if (state.pointedAction.isTimedAction()) {
            state.navigator.navigateTo(TimedActionActivity.class);
        } else if (state.pointedAction.isRepsAction()) {
            // TODO navigator.navigateTo(RepsActionActivity.class);
        } else if (state.pointedAction.isTimedRecoveryAction()) {
            state.navigator.navigateTo(TimedRecoveryActivity.class);
        } else if (state.pointedAction.isRecoveryAction()) {
            // TODO navigator.navigateTo(TimedRecoveryActivity.class);
        }
    }

    private void startTimer() {

        if (timer == null) {

            timer = new CountDownTimer(Long.MAX_VALUE, 1000L) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (!tts.isSpeaking()) {
                        if (count < 0) {
                            cancel();
                            navigateToTrainer();
                        } else if (count > 0) {
                            title.setVisibility(View.VISIBLE);
                            label.setVisibility(View.VISIBLE);
                            labelHidden.setVisibility(View.INVISIBLE);
                            tts.speak("" + count, TextToSpeech.QUEUE_FLUSH, null, "" + count);
                            label.setText(String.valueOf(count));
                        } else {
                            tts.speak("Go!", TextToSpeech.QUEUE_FLUSH, null, "0");
                            title.setVisibility(View.INVISIBLE);
                            label.setVisibility(View.INVISIBLE);
                            labelHidden.setVisibility(View.VISIBLE);
                        }

                        count--;
                    }
                }

                @Override
                public void onFinish() {
                    // do nothing
                }
            };
        }

        timer.start();
    }
}
