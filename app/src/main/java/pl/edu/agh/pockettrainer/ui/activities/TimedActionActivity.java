package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Locale;

import pl.edu.agh.pockettrainer.BufferedMediaPlayer;
import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class TimedActionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = new Logger(TimedActionActivity.class);

    private BufferedMediaPlayer beatSound;
    private CountDownTimer timer;
    private TimedAction timedAction;
    private TextView labelSeconds;
    private int numSeconds;
    private TextToSpeech tts;
    private ApplicationState state;
    private Progress progress;

    @Override
    public void onBackPressed() {
        // prevent from interrupting the countdown
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timed_action);
    }

    @Override
    protected void onResume() {

        state = (ApplicationState) getApplicationContext();
        progress = state.getProgress();

        tts = new TextToSpeech(this, this);

        final ProgressBar progressBar = findViewById(R.id.timed_action_progressBar);
        progressBar.setProgress(state.getDayProgressPercentage());

        final ImageView imageView = findViewById(R.id.timed_action_image);
        final TextView title = findViewById(R.id.timed_action_title);
        final TextView label = findViewById(R.id.timed_action_label);
        labelSeconds = findViewById(R.id.timed_action_seconds);

        timedAction = (TimedAction) state.pointedAction.action;
        final Exercise exercise = timedAction.getExercise();

        setImage(imageView, exercise.getImage());
        title.setText(capitalize(exercise.getName()));
        label.setText(timedAction.getSeconds() + " seconds");

        resetTimer();
        labelSeconds.setText(String.valueOf(numSeconds));

        resetTimer();

        super.onResume();
    }

    @Override
    protected void onPause() {
        stopTimer();
        beatSound.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        stopTimer();
        beatSound.stop();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopTimer();
        beatSound.stop();
        super.onStop();
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

    public void onSkipButtonClick(View view) {
        stopTimer();
        beatSound.stop();
        progress.skipAction();
        state.navigator.navigateToNextAction(progress);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void resetTimer() {
        numSeconds = timedAction.getSeconds();
    }

    private void startTimer() {

        if (timer == null) {

            beatSound = BufferedMediaPlayer.create(this, R.raw.beat);
            beatSound.start();

            timer = new CountDownTimer((1 + timedAction.getSeconds()) * 1000L, 1000L) {

                @Override
                public void onTick(long millisUntilFinished) {
                    labelSeconds.setText(String.valueOf(numSeconds--));

                    int secondsLeft = (int) (millisUntilFinished / 1000L);

                    if (secondsLeft <= 10) {
                        tts.speak("" + secondsLeft, TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                    } else {
                        if (secondsLeft % 30 == 0) {
                            tts.speak(secondsLeft + " seconds", TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    beatSound.stop();
                    progress.finishAction();
                    state.navigator.navigateToNextAction(progress);
                }
            };

            progress.startAction();
            timer.start();
        }
    }

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
