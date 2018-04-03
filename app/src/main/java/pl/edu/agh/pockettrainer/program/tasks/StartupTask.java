package pl.edu.agh.pockettrainer.program.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.activities.TodayNoneActivity;
import pl.edu.agh.pockettrainer.ui.activities.ProgramBrowserActivity;

public class StartupTask implements Runnable {

    private final Context context;

    public StartupTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {

        final AppConfig appConfig = new AppConfig(context);
        final ProgramRepository programs = ProgramRepositoryFactory.getCachedFileRepository(context);

        if (appConfig.isFirstRun()) {
            for (String name : programs.getBundledArchives()) {
                programs.installResource(name);
            }
        }

        programs.getInstalled();  // force load all while still showing splash screen

        if (programs.hasActiveProgram()) {
            navigateTo(TodayNoneActivity.class);
        } else {
            navigateTo(ProgramBrowserActivity.class);
        }
    }

    private void navigateTo(Class<? extends Activity> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }
}
