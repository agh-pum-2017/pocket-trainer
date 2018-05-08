package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
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
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class TimedActionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = new Logger(TimedActionActivity.class);

    private BufferedMediaPlayer beatSound;
    private CountDownTimer timer;
    private TimedAction timedAction;
    private TextView labelSeconds;
    private int maxSeconds;
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

        initComingNext();

        resetTimer();
        labelSeconds.setText(String.valueOf(numSeconds));

        tts = new TextToSpeech(this, this);

        resetTimer();

        super.onResume();
    }

    private void initComingNext() {

        final TextView comingLabel = findViewById(R.id.timed_action_coming_label);
        final ImageView nextImageView = findViewById(R.id.timed_action_next_image);
        final ImageView nextIconView = findViewById(R.id.timed_action_next_icon);
        final TextView nextTitle = findViewById(R.id.timed_action_next_title);
        final TextView nextLabel = findViewById(R.id.timed_action_next_label);
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
        navigateToNextAction();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void resetTimer() {
        numSeconds = maxSeconds = timedAction.getSeconds();
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
                        if (!tts.isSpeaking()) {
                            tts.speak("" + secondsLeft, TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                        }
                    } else {
                        if (numSeconds == maxSeconds - 1) {
                            tts.speak(secondsLeft + " seconds", TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                        } else if (secondsLeft % 30 == 0) {
                            if (!tts.isSpeaking()) {
                                tts.speak(secondsLeft + " seconds", TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                            }
                        }
                    }
                }

                @Override
                public void onFinish() {
                    beatSound.stop();
                    progress.finishAction();
                    navigateToNextAction();
                }
            };

            progress.startAction();
            timer.start();
        }
    }

    private void navigateToNextAction() {
        if (state.isEndOfWorkout(progress)) {
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
}
