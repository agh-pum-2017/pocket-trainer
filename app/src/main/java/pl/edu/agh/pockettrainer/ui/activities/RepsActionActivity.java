package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import pl.edu.agh.pockettrainer.ui.TripleTapListener;

public class RepsActionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = new Logger(RepsActionActivity.class);

    private ApplicationState state;
    private Progress progress;
    private BufferedMediaPlayer beatSound;
    private TextToSpeech tts;
    private String message;
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
        setContentView(R.layout.activity_reps_action);
    }

    @Override
    protected void onPause() {

        if (beatSound != null) {
            beatSound.stop();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (beatSound != null) {
            beatSound.stop();
        }

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {

        if (beatSound != null) {
            beatSound.stop();
        }

        super.onStop();
    }

    @Override
    protected void onResume() {

        state = (ApplicationState) getApplicationContext();
        progress = state.getProgress();
        progress.startAction();

        final ProgressBar progressBar = findViewById(R.id.reps_action_progressBar);
        progressBar.setProgress(state.getDayProgressPercentage());

        final ImageView imageView = findViewById(R.id.reps_action_image);
        final RepsAction repsAction = (RepsAction) state.pointedAction.action;
        final Exercise exercise = repsAction.getExercise();
        setImage(imageView, exercise.getImage());

        final TextView title = findViewById(R.id.reps_action_title);
        title.setText(capitalize(exercise.getName()));

        final TextView label = findViewById(R.id.reps_action_label);
        label.setText(String.valueOf(repsAction.getReps()));

        if (state.appConfig.isSoundEnabled()) {
            beatSound = BufferedMediaPlayer.create(this, R.raw.beat);
            beatSound.start();
        }

        message = exercise.getName() + ", " + repsAction.getReps() + " times";

        final TextView stepLabel = findViewById(R.id.reps_action_step);
        stepLabel.setText(state.getDayActionNumber() + " / " + state.getDayActionsCount(progress));

        initComingNext();

        if (state.appConfig.isVoiceEnabled()) {
            tts = new TextToSpeech(this, this);
        }

        tripleTapListener = new TripleTapListener(new Runnable() {

            @Override
            public void run() {

                if (beatSound != null) {
                    beatSound.stop();
                }

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

        final TextView comingLabel = findViewById(R.id.reps_action_coming_label);
        final ImageView nextImageView = findViewById(R.id.reps_action_next_image);
        final ImageView nextIconView = findViewById(R.id.reps_action_next_icon);
        final TextView nextTitle = findViewById(R.id.reps_action_next_title);
        final TextView nextLabel = findViewById(R.id.reps_action_next_label);
        nextImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (state.futurePointedAction != null) {

            comingLabel.setVisibility(View.VISIBLE);
            nextImageView.setVisibility(View.VISIBLE);
            nextTitle.setVisibility(View.VISIBLE);
            nextIconView.setVisibility(View.VISIBLE);
            nextLabel.setVisibility(View.VISIBLE);

            if (state.futurePointedAction.isTimedAction()) {
                final TimedAction nextTimedAction = (TimedAction) state.futurePointedAction.action;
                final Exercise nextExercise = nextTimedAction.getExercise();
                setImage(nextImageView, nextExercise.getImage());
                nextTitle.setText(capitalize(nextExercise.getName()));
                nextIconView.setImageResource(R.drawable.ic_watch);
                nextLabel.setText(nextTimedAction.getSeconds() + " seconds");
            } else if (state.futurePointedAction.isRepsAction()) {
                final RepsAction nextRepsAction = (RepsAction) state.futurePointedAction.action;
                final Exercise nextExercise = nextRepsAction.getExercise();
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
            comingLabel.setVisibility(View.INVISIBLE);
            nextImageView.setVisibility(View.INVISIBLE);
            nextTitle.setVisibility(View.INVISIBLE);
            nextIconView.setVisibility(View.INVISIBLE);
            nextLabel.setVisibility(View.INVISIBLE);
        }
    }

    public void onDonButtonClick(View view) {

        if (beatSound != null) {
            beatSound.stop();
        }

        progress.finishAction();
        navigateToNextAction();
    }

    public void onSkipButtonClick(View view) {

        if (beatSound != null) {
            beatSound.stop();
        }

        progress.skipAction();
        navigateToNextAction();
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
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "reps_action");
            }
        } else {
            logger.error("Unable to initialize TTS");
        }
    }

    private void navigateToNextAction() {
        if (tts != null && state.isEndOfWorkout(progress)) {
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
}
