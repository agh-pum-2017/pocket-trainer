package pl.edu.agh.pockettrainer.ui;

import android.app.Application;
import android.content.Context;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.program.repository.progress.ProgressRepository;
import pl.edu.agh.pockettrainer.program.tasks.StartupTask;

public class ApplicationState extends Application {

    public static ApplicationState getInstance(Context context) {

        final ApplicationState state = (ApplicationState) context.getApplicationContext();

        state.appConfig = new AppConfig(context);
        state.metaRepository = new DefaultMetaRepository(context);
        state.programRepository = state.metaRepository.getProgramRepository();
        state.progressRepository = state.metaRepository.getProgressRepository();
        state.navigator = new Navigator(context);

        return state;
    }

    public AppConfig appConfig;
    public MetaRepository metaRepository;
    public ProgramRepository programRepository;
    public ProgressRepository progressRepository;
    public Navigator navigator;

    public Action action;
    public Action futureAction;

    public Progress getProgress() {
        return programRepository.getActiveProgram().getProgress();
    }
}
