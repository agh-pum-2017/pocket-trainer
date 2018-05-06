package pl.edu.agh.pockettrainer.program.tasks;

import android.content.Context;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.Navigator;
import pl.edu.agh.pockettrainer.ui.activities.ProgramBrowserActivity;

public class StartupTask implements Runnable {

    private final Context context;

    public StartupTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {

        final ApplicationState state = ApplicationState.getInstance(context);

        if (state.appConfig.isFirstRun()) {
            for (String name : state.programRepository.getBundledArchives()) {
                state.programRepository.installResource(name);
            }
        }

        state.programRepository.getInstalled();  // force load all while still showing splash screen

        final Navigator navigator = new Navigator(context);
        if (state.programRepository.hasActiveProgram()) {
            navigator.navigateToToday(state.programRepository.getActiveProgram());
        } else {
            navigator.navigateTo(ProgramBrowserActivity.class);
        }
    }
}
