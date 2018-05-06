package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Locale;

import pl.edu.agh.pockettrainer.BufferedMediaPlayer;
import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class TimedRecoveryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // TODO TTS recovery time 30 seconds...
    // TODO TTS next exercise: "push-ups"
    // TODO destroy() shutdown TTS

    private final Logger logger = new Logger(TimedRecoveryActivity.class);

    private BufferedMediaPlayer tickTockSound;
    private CountDownTimer timer;
    private TextView secondsLabel;
    private TimedRecovery timedRecoveryAction;
    private int numSeconds;
    private Progress progress;
    private ApplicationState state;
    private TextToSpeech tts;
    private String nextExerciseMessage;

    @Override
    public void onBackPressed() {
        // prevent from interrupting timed recovery
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timed_recovery);

        state = (ApplicationState) getApplicationContext();
        progress = state.getProgress();

        tts = new TextToSpeech(this, this);

        timedRecoveryAction = (TimedRecovery) state.pointedAction.action;
        secondsLabel = findViewById(R.id.timed_recovery_seconds);
        secondsLabel.setText(String.valueOf(timedRecoveryAction.getSeconds()));

        final TextView comingLabel = findViewById(R.id.timed_recovery_coming_label);
        final ImageView imageView = findViewById(R.id.timed_recovery_next_image);
        final ImageView iconView = findViewById(R.id.timed_recovery_next_icon);
        final TextView title = findViewById(R.id.timed_recovery_next_title);
        final TextView label = findViewById(R.id.timed_recovery_next_label);

        if (state.futurePointedAction != null) {

            comingLabel.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);

            if (state.futurePointedAction.isTimedAction()) {
                final TimedAction timedAction = (TimedAction) state.futurePointedAction.action;
                final Exercise exercise = timedAction.getExercise();
                nextExerciseMessage = "Next exercise: " + exercise.getName();
                setImage(imageView, exercise.getImage());
                iconView.setImageResource(R.drawable.ic_watch);
                title.setText(capitalize(exercise.getName()));
                label.setText(timedAction.getSeconds() + " seconds");
            } else if (state.futurePointedAction.isRepsAction()) {
                final RepsAction repsAction = (RepsAction) state.futurePointedAction.action;
                final Exercise exercise = repsAction.getExercise();
                nextExerciseMessage = "Next exercise: " + exercise.getName();
                setImage(imageView, exercise.getImage());
                iconView.setImageResource(R.drawable.ic_reps);
                title.setText(capitalize(exercise.getName()));
                label.setText(repsAction.getReps() + " reps");
            } else if (state.futurePointedAction.isTimedAction()) {
                final TimedRecovery timedRecovery = (TimedRecovery) state.futurePointedAction.action;
                iconView.setImageResource(R.drawable.ic_watch);
                title.setText("Timed recovery");
                label.setText(timedRecovery.getSeconds() + " seconds");
            } else if (state.futurePointedAction.isRecoveryAction()) {
                final Recovery recovery = (Recovery) state.futurePointedAction.action;
                iconView.setImageResource(R.drawable.ic_reps);
                title.setText("Recovery");
                label.setText("");
            }
        } else {
            comingLabel.setVisibility(View.INVISIBLE);
            iconView.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            label.setVisibility(View.INVISIBLE);
        }

        resetTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetTimer();
    }

    @Override
    protected void onPause() {
        stopTimer();
        tickTockSound.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        stopTimer();
        tickTockSound.stop();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopTimer();
        tickTockSound.stop();
        super.onStop();
    }

    private void resetTimer() {
        numSeconds = timedRecoveryAction.getSeconds();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void startTimer() {

        if (timer == null) {

            tickTockSound = BufferedMediaPlayer.create(this, R.raw.tick_tock);
            tickTockSound.start();

            String message = "Recovery time";
            if (nextExerciseMessage != null) {
                message += ". " + nextExerciseMessage;
            }

            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "recovery_time");

            timer = new CountDownTimer((1 + timedRecoveryAction.getSeconds()) * 1000L, 1000L) {

                @Override
                public void onTick(long millisUntilFinished) {

                    secondsLabel.setText(String.valueOf(numSeconds--));

                    int secondsLeft = (int) (millisUntilFinished / 1000L);

                    if (secondsLeft <= 0) {
                        secondsLabel.setText(R.string.go);
                        tts.speak("Go!", TextToSpeech.QUEUE_FLUSH, null, "go");
                    } else if (secondsLeft <= 3) {
                        tts.speak("" + secondsLeft, TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                    } else {
                        if (secondsLeft % 30 == 0) {
                            tts.speak(secondsLeft + " seconds", TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    tickTockSound.stop();
                    progress.finishAction();
                    state.navigator.navigateToNextAction(progress);
                }
            };

            progress.startAction();
            timer.start();
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

    public void onSkipButtonClick(View view) {
        stopTimer();
        tickTockSound.stop();
        progress.skipAction();
        state.navigator.navigateToNextAction(progress);
    }

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
