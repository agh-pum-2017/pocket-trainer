package pl.edu.agh.pockettrainer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.activities.RecoveryActionActivity;
import pl.edu.agh.pockettrainer.ui.activities.RepsActionActivity;
import pl.edu.agh.pockettrainer.ui.activities.TimedActionActivity;
import pl.edu.agh.pockettrainer.ui.activities.TimedRecoveryActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayBelatedActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayFinishedActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayNewActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayReadyActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayRecoveryActivity;

public class Navigator {

    private final Logger logger = new Logger(Navigator.class);

    private final Context context;
    private TextToSpeech tts;

    public Navigator(Context context) {
        this.context = context;
    }

    public void navigateTo(Class<? extends Activity> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }

    public void navigateToNextAction(Progress progress) {
        if (progress.getState() == ProgressState.IN_PROGRESS) {
            final ApplicationState state = (ApplicationState) context.getApplicationContext();
            state.pointedAction = progress.getNextAction();
            state.futurePointedAction = null;

            if (state.pointedAction == null) {
                navigateToToday(progress.getProgram());
            } else {
                if (state.pointedAction.isRecovery()) {

                    state.futurePointedAction = state.getProgress().getFutureAction();

                    if (state.pointedAction.isTimedRecoveryAction()) {
                        state.navigator.navigateTo(TimedRecoveryActivity.class);
                    } else if (state.pointedAction.isRecoveryAction()) {
                        state.navigator.navigateTo(RecoveryActionActivity.class);
                    }
                } else {
                    if (state.pointedAction.isTimedAction()) {
                        state.navigator.navigateTo(TimedActionActivity.class);
                    } else if (state.pointedAction.isRepsAction()) {
                        state.navigator.navigateTo(RepsActionActivity.class);
                    }
                }
            }
        } else {
            speakEndOfWorkout();
            navigateToToday(progress.getProgram());
        }
    }

    public void navigateToToday(Program program) {
        final Progress progress = program.getProgress();
        final ProgressState state = progress.getState();
        logger.info("Training program state is \"%s\"", state.name());
        switch (state) {
            case NEW:
                navigateTo(TodayNewActivity.class);
                break;
            case READY:
                navigateTo(TodayReadyActivity.class);
                break;
            case BELATED:
                navigateTo(TodayBelatedActivity.class);
                break;
            case RECOVERY:
                navigateTo(TodayRecoveryActivity.class);
                break;
            case IN_PROGRESS:
                navigateTo(TodayReadyActivity.class);
                break;
            case FINISHED:
                navigateTo(TodayFinishedActivity.class);
                break;
        }
    }

    private void speakEndOfWorkout() {

        final TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    final int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        logger.error("TTS language is not supported: US English");
                    } else {
                        tts.speak("End of workout.", TextToSpeech.QUEUE_FLUSH, null, "end_of_workout");
                    }
                } else {
                    logger.error("Unable to initialize TTS");
                }
            }
        };

        tts = new TextToSpeech(context.getApplicationContext(), initListener);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if ("end_of_workout".equals(utteranceId)) {
                    tts.stop();
                    tts.shutdown();
                }
            }

            @Override
            public void onError(String utteranceId) {
                tts.stop();
                tts.shutdown();
            }
        });
    }
}
