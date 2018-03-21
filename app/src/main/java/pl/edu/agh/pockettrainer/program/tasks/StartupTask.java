package pl.edu.agh.pockettrainer.program.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.repository.ProgramFileRepository;
import pl.edu.agh.pockettrainer.program.repository.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.TrainingProgramWithId;
import pl.edu.agh.pockettrainer.ui.HomeActivity;
import pl.edu.agh.pockettrainer.ui.ProgramBrowserActivity;

public class StartupTask implements Runnable {

    private final Logger logger = new Logger(StartupTask.class);
    private final Context context;

    public StartupTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {

        final AppConfig appConfig = new AppConfig(context);
        final ProgramRepository programs = new ProgramFileRepository(context);

        if (appConfig.isFirstRun()) {
            logger.debug("Application is started for the first time");
            for (String name : programs.getBundledArchives()) {
                programs.installResource(name);
            }
            // programs.installRemoteFile("http://192.168.0.12:8080/remote.zip");
        }

        if (programs.hasActiveProgram()) {
            final TrainingProgramWithId program = programs.getActiveProgram();
            // TODO load progress
            // TODO calculate some other stuff
            // display active program
            navigateTo(HomeActivity.class);
        } else {
            navigateTo(ProgramBrowserActivity.class);
        }
    }

    private void navigateTo(Class<? extends Activity> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }
}
