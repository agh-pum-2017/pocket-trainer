package pl.edu.agh.pockettrainer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.activities.TodayBelatedActivity;
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
        final Progress progress = program.getProgress();
        switch (progress.getState()) {
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
                throw new RuntimeException("Not implemented");
            case FINISHED:
                navigateTo(TodayFinishedActivity.class);
                break;
        }
    }
}
