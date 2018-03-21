package pl.edu.agh.pockettrainer.program.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.repository.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.ProgramFileRepository;
import pl.edu.agh.pockettrainer.program.repository.ProgramRepository;
import pl.edu.agh.pockettrainer.ui.activities.HomeActivity;
import pl.edu.agh.pockettrainer.ui.activities.ProgramBrowserActivity;

public class StartupTask implements Runnable {

    private final Context context;

    public StartupTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {

        final AppConfig appConfig = new AppConfig(context);
        final ProgramRepository programs = CachedProgramRepository.getInstance(context);

        if (appConfig.isFirstRun()) {
            for (String name : programs.getBundledArchives()) {
                programs.installResource(name);
            }
        }

        programs.getInstalled();

//        try {
//            Thread.sleep(100L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        navigateTo(ProgramBrowserActivity.class);

//        if (programs.hasActiveProgram()) {
//            navigateTo(HomeActivity.class);
//        } else {
//            navigateTo(ProgramBrowserActivity.class);
//        }
    }

    private void navigateTo(Class<? extends Activity> activityClass) {

        // TODO disable full screen mode

        context.startActivity(new Intent(context, activityClass));
    }
}
