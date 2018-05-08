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

public class TimedRecoveryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

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
        setContentView(R.layout.activity_timed_recovery);

        state = (ApplicationState) getApplicationContext();
        progress = state.getProgress();

        timedRecoveryAction = (TimedRecovery) state.pointedAction.action;
        secondsLabel = findViewById(R.id.timed_recovery_seconds);
        secondsLabel.setText(String.valueOf(timedRecoveryAction.getSeconds()));

        final ProgressBar progressBar = findViewById(R.id.timed_recovery_next_progressBar);
        progressBar.setProgress(state.getDayProgressPercentage());

        final TextView stepLabel = findViewById(R.id.timed_recovery_step);
        stepLabel.setText(state.getDayActionNumber() + " / " + state.getDayActionsCount(progress));

        initComingNext();

        if (state.appConfig.isVoiceEnabled()) {
            tts = new TextToSpeech(this, this);
        } else {
            startTimer();
        }

        tripleTapListener = new TripleTapListener(new Runnable() {

            @Override
            public void run() {

                stopTimer();

                if (tickTockSound != null) {
                    tickTockSound.stop();
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

        resetTimer();
    }

    private void initComingNext() {

        final TextView comingLabel = findViewById(R.id.timed_recovery_coming_label);
        final ImageView nextImageView = findViewById(R.id.timed_recovery_next_image);
        final ImageView nextIconView = findViewById(R.id.timed_recovery_next_icon);
        final TextView nextTitle = findViewById(R.id.timed_recovery_next_title);
        final TextView nextLabel = findViewById(R.id.timed_recovery_next_label);
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
            comingLabel.setVisibility(View.INVISIBLE);
            nextImageView.setVisibility(View.INVISIBLE);
            nextTitle.setVisibility(View.INVISIBLE);
            nextIconView.setVisibility(View.INVISIBLE);
            nextLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetTimer();
    }

    @Override
    protected void onPause() {

        stopTimer();

        if (tickTockSound != null) {
            tickTockSound.stop();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        stopTimer();

        if (tickTockSound != null) {
            tickTockSound.stop();
        }

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {

        stopTimer();

        if (tickTockSound != null) {
            tickTockSound.stop();
        }

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

            if (state.appConfig.isSoundEnabled()) {
                tickTockSound = BufferedMediaPlayer.create(this, R.raw.tick_tock);
                tickTockSound.start();
            }

            String message = "Recovery time";
            if (nextExerciseMessage != null) {
                message += ". " + nextExerciseMessage;
            }

            if (tts != null) {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "recovery_time");
            }

            timer = new CountDownTimer((1 + timedRecoveryAction.getSeconds()) * 1000L, 1000L) {

                @Override
                public void onTick(long millisUntilFinished) {

                    secondsLabel.setText(String.valueOf(numSeconds--));

                    int secondsLeft = (int) (millisUntilFinished / 1000L);

                    if (secondsLeft <= 0) {
                        secondsLabel.setText(R.string.go);
                        if (tts != null && !tts.isSpeaking()) {
                            tts.speak("Go!", TextToSpeech.QUEUE_FLUSH, null, "go");
                        }
                    } else if (tts != null && secondsLeft <= 3) {
                        if (!tts.isSpeaking()) {
                            tts.speak("" + secondsLeft, TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                        }
                    } else {
                        if (secondsLeft % 30 == 0) {
                            if (tts != null && !tts.isSpeaking()) {
                                tts.speak(secondsLeft + " seconds", TextToSpeech.QUEUE_FLUSH, null, "" + secondsLeft);
                            }
                        }
                    }
                }

                @Override
                public void onFinish() {

                    if (tickTockSound != null) {
                        tickTockSound.stop();
                    }

                    progress.finishAction();
                    navigateToNextAction();
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

        if (tickTockSound != null) {
            tickTockSound.stop();
        }

        progress.skipAction();
        navigateToNextAction();
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

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase().replaceAll("[_-]", " ");
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
