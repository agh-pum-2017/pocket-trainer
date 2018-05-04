package pl.edu.agh.pockettrainer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.ui.activities.TodayFinishedActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayNewActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayReadyActivity;
import pl.edu.agh.pockettrainer.ui.activities.TodayRecoveryActivity;

public class Navigator {

    private final Context context;

    public Navigator(Context context) {
        this.context = context;
    }

    public void navigateTo(Class<? extends Activity> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }

    public void navigateToToday(Program program) {

        ProgressState progress = ProgressState.RECOVERY; // TODO program.getProgress();

        switch (progress) {
            case NEW:
                navigateTo(TodayNewActivity.class);
                break;
            case READY:
                navigateTo(TodayReadyActivity.class);
                break;
            case BELATED:
                throw new RuntimeException("Not implemented"); // TODO
            // TODO case IN_PROGRESS:
            case RECOVERY:
                navigateTo(TodayRecoveryActivity.class);
                break;
            case FINISHED:
                navigateTo(TodayFinishedActivity.class);
                break;
        }

    }
}
