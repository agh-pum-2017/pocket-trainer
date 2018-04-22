package pl.edu.agh.pockettrainer.program.tasks;

import android.content.Context;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.Navigator;
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

        final Navigator navigator = new Navigator(context);
        if (programs.hasActiveProgram()) {
            navigator.navigateToToday(programs.getActiveProgram());
        } else {
            navigator.navigateTo(ProgramBrowserActivity.class);
        }
    }
}
