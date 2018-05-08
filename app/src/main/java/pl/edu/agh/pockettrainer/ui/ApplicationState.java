package pl.edu.agh.pockettrainer.ui;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.iterator.PointedAction;
import pl.edu.agh.pockettrainer.program.repository.program.iterator.Pointer;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.program.repository.progress.ProgressRepository;

public class ApplicationState extends Application {

    public static ApplicationState getInstance(Context context) {

        final ApplicationState state = (ApplicationState) context.getApplicationContext();

        state.appConfig = new AppConfig(context);
        state.metaRepository = new DefaultMetaRepository(context);
        state.programRepository = state.metaRepository.getProgramRepository();
        state.progressRepository = state.metaRepository.getProgressRepository();
        state.navigator = new Navigator(context);
        state.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        return state;
    }

    public AppConfig appConfig;
    public MetaRepository metaRepository;
    public ProgramRepository programRepository;
    public ProgressRepository progressRepository;
    public Navigator navigator;

    public PointedAction pointedAction;
    public PointedAction futurePointedAction;

    private Vibrator vibrator;

    public void vibrateShort() {
        vibrate(200L);
    }

    public void vibrateLong() {
        vibrate(500L);
    }

    private void vibrate(long milliseconds) {
        if (appConfig.isVibrateEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(milliseconds);
            }
        }
    }

    public Progress getProgress() {
        return programRepository.getActiveProgram().getProgress();
    }

    public int getDayProgressPercentage() {

        if (pointedAction != null) {

            final Pointer pointer = pointedAction.pointer;
            final int numDayActions = programRepository.getActiveProgram().getSchedule().get(pointer.dayIndex).getNumActions();

            if (numDayActions > 0) {
                return (int) (100.0 * (pointer.actionIndex + 1) / numDayActions);
            } else {
                return 0;
            }
        }

        return 0;
    }

    public boolean isEndOfWorkout(Progress progress) {
        return futurePointedAction == null;
    }

    public int getDayActionNumber() {
        return pointedAction.pointer.actionIndex + 1;
    }

    public int getDayActionsCount(Progress progress) {
        final Day day = progress.getProgram().getSchedule().get(pointedAction.pointer.dayIndex);
        return day.getNumActions();
    }
}
