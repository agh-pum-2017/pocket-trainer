package pl.edu.agh.pockettrainer.ui.activities;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Locale;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class RecoveryActionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = new Logger(RecoveryActionActivity.class);

    private ApplicationState state;
    private Progress progress;
    private Recovery recovery;
    private TextToSpeech tts;
    private String nextExerciseMessage;

    @Override
    public void onBackPressed() {
        // prevent from interrupting the countdown
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_action);
    }

    @Override
    protected void onResume() {

        state = (ApplicationState) getApplicationContext();
        progress = state.getProgress();
        progress.startAction();

        recovery = (Recovery) state.pointedAction.action;

        final ProgressBar progressBar = findViewById(R.id.recovery_action_next_progressBar);
        progressBar.setProgress(state.getDayProgressPercentage());

        final TextView comingLabel = findViewById(R.id.recovery_action_coming_label);
        final ImageView imageView = findViewById(R.id.recovery_action_next_image);
        final ImageView iconView = findViewById(R.id.recovery_action_next_icon);
        final TextView title = findViewById(R.id.recovery_action_next_title);
        final TextView label = findViewById(R.id.recovery_action_next_label);

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

        tts = new TextToSpeech(this, this);

        super.onResume();
    }

    @Override
    protected void onDestroy() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    public void onDoneButtonClick(View view) {

        progress.finishAction();

        if (state.futurePointedAction == null) {
            tts.speak("End of workout", TextToSpeech.QUEUE_FLUSH, null, "end_of_workout");
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    // do nothing
                }

                @Override
                public void onDone(String utteranceId) {
                    if ("end_of_workout".equals(utteranceId)) {
                        state.navigator.navigateToNextAction(progress);
                    }
                }

                @Override
                public void onError(String utteranceId) {
                    // do nothing
                }
            });
        } else {
            state.navigator.navigateToNextAction(progress);
        }
    }

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase().replaceAll("[_-]", " ");
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            final int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                logger.error("TTS language is not supported: US English");
            } else {

                String message = "Recovery";
                if (nextExerciseMessage != null) {
                    message += ". " + nextExerciseMessage;
                }

                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "recovery_action");
            }
        } else {
            logger.error("Unable to initialize TTS");
        }
    }
}
