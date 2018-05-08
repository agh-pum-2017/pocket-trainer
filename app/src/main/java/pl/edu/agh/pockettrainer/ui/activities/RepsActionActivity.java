package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
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
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class RepsActionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = new Logger(RepsActionActivity.class);

    private ApplicationState state;
    private Progress progress;
    private BufferedMediaPlayer beatSound;
    private TextToSpeech tts;
    private String message;

    @Override
    public void onBackPressed() {
        // prevent from interrupting the countdown
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps_action);
    }

    @Override
    protected void onPause() {
        beatSound.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        beatSound.stop();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        beatSound.stop();
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

        beatSound = BufferedMediaPlayer.create(this, R.raw.beat);
        beatSound.start();

        message = exercise.getName() + ", " + repsAction.getReps() + " times";

        tts = new TextToSpeech(this, this);

        super.onResume();
    }

    public void onDonButtonClick(View view) {
        beatSound.stop();
        progress.finishAction();
        state.navigator.navigateToNextAction(progress);
    }

    public void onSkipButtonClick(View view) {
        beatSound.stop();
        progress.skipAction();
        state.navigator.navigateToNextAction(progress);
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
}
