package pl.edu.agh.pockettrainer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
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

    public void navigateToNextAction(Progress progress, Action action) {

        final ApplicationState state = (ApplicationState) context.getApplicationContext();
        state.action = state.getProgress().getNextAction();
        state.futureAction = null;

        if (state.action == null) {
            navigateTo(TodayFinishedActivity.class);
        } else {
            if (state.action.isRecovery()) {

                state.futureAction = state.getProgress().getFutureAction();

                if (state.action instanceof TimedRecovery) {
                    state.navigator.navigateTo(TimedRecoveryActivity.class);
                } else if (state.action instanceof Recovery) {
                    // TODO navigator.navigateTo(TimedRecoveryActivity.class);
                }
            } else {
                if (state.action instanceof TimedAction) {
                    state.navigator.navigateTo(TimedActionActivity.class);
                } else if (state.action instanceof RepsAction) {
                    // TODO navigator.navigateTo(RepsActionActivity.class);
                }
            }
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
