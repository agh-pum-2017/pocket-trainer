package pl.edu.agh.pockettrainer.ui.activities;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import pl.edu.agh.pockettrainer.ui.TripleTapListener;

public class RecoveryActionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = new Logger(RecoveryActionActivity.class);

    private ApplicationState state;
    private Progress progress;
    private Recovery recovery;
    private TextToSpeech tts;
    private String nextExerciseMessage;
    private TripleTapListener tripleTapListener;

    @Override
    public void onBackPressed() {
        if (tripleTapListener != null) {
            tripleTapListener.tap();
        }
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

        final TextView stepLabel = findViewById(R.id.recovery_action_step);
        stepLabel.setText(state.getDayActionNumber() + " / " + state.getDayActionsCount(progress));

        initComingNext();

        if (state.appConfig.isVoiceEnabled()) {
            tts = new TextToSpeech(this, this);
        }

        tripleTapListener = new TripleTapListener(new Runnable() {

            @Override
            public void run() {

                if (tts != null) {
                    tts.stop();
                    tts.shutdown();
                }

                progress.abort();
                Toast.makeText(getApplicationContext(), "Training interrupted", Toast.LENGTH_SHORT).show();
                state.navigator.navigateToToday(progress.getProgram());
            }
        });

        super.onResume();
    }

    private void initComingNext() {

        final TextView comingLabel = findViewById(R.id.recovery_action_coming_label);
        final ImageView nextImageView = findViewById(R.id.recovery_action_next_image);
        final ImageView nextIconView = findViewById(R.id.recovery_action_next_icon);
        final TextView nextTitle = findViewById(R.id.recovery_action_next_title);
        final TextView nextLabel = findViewById(R.id.recovery_action_next_label);
        final Button readyButton = findViewById(R.id.recovery_action_next_button_done);
        nextImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (state.futurePointedAction != null) {

            readyButton.setText(R.string.ready);
            comingLabel.setVisibility(View.VISIBLE);
            nextImageView.setVisibility(View.VISIBLE);
            nextTitle.setVisibility(View.VISIBLE);
            nextIconView.setVisibility(View.VISIBLE);
            nextLabel.setVisibility(View.VISIBLE);

            if (state.futurePointedAction.isTimedAction()) {
                final TimedAction nextTimedAction = (TimedAction) state.futurePointedAction.action;
                final Exercise nextExercise = nextTimedAction.getExercise();
                nextExerciseMessage = "Next exercise: " + nextExercise.getName();
                setImage(nextImageView, nextExercise.getImage());
                nextTitle.setText(capitalize(nextExercise.getName()));
                nextIconView.setImageResource(R.drawable.ic_watch);
                nextLabel.setText(nextTimedAction.getSeconds() + " seconds");
            } else if (state.futurePointedAction.isRepsAction()) {
                final RepsAction nextRepsAction = (RepsAction) state.futurePointedAction.action;
                final Exercise nextExercise = nextRepsAction.getExercise();
                nextExerciseMessage = "Next exercise: " + nextExercise.getName();
                setImage(nextImageView, nextExercise.getImage());
                nextIconView.setImageResource(R.drawable.ic_reps);
                nextTitle.setText(capitalize(nextExercise.getName()));
                nextLabel.setText(nextRepsAction.getReps() + " reps");
            } else if (state.futurePointedAction.isTimedRecoveryAction()) {
                final TimedRecovery nextTimedRecovery = (TimedRecovery) state.futurePointedAction.action;
                nextImageView.setImageResource(R.drawable.ic_battery);
                nextImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                nextIconView.setImageResource(R.drawable.ic_watch);
                nextTitle.setText("Timed recovery");
                nextLabel.setText(nextTimedRecovery.getSeconds() + " seconds");
            } else if (state.futurePointedAction.isRecoveryAction()) {
                final Recovery nextRecovery = (Recovery) state.futurePointedAction.action;
                nextImageView.setImageResource(R.drawable.ic_battery);
                nextImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                nextIconView.setImageResource(R.drawable.ic_reps);
                nextTitle.setText("Recovery");
                nextLabel.setText("");
            }
        } else {
            readyButton.setText(R.string.done);
            comingLabel.setVisibility(View.INVISIBLE);
            nextImageView.setVisibility(View.INVISIBLE);
            nextTitle.setVisibility(View.INVISIBLE);
            nextIconView.setVisibility(View.INVISIBLE);
            nextLabel.setVisibility(View.INVISIBLE);
        }
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

        if (state.isEndOfWorkout(progress)) {
            if (tts != null) {
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
                state.vibrateLong();
            } else {
                state.vibrateLong();
                state.navigator.navigateToNextAction(progress);
            }
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
