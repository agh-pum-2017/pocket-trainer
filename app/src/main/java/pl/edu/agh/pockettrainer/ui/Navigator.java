package pl.edu.agh.pockettrainer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
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
                        // TODO navigator.navigateTo(TimedRecoveryActivity.class);
                    }
                } else {
                    if (state.pointedAction.isTimedAction()) {
                        state.navigator.navigateTo(TimedActionActivity.class);
                    } else if (state.pointedAction.isRepsAction()) {
                        // TODO navigator.navigateTo(RepsActionActivity.class);
                    }
                }
            }
        } else {
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
}
