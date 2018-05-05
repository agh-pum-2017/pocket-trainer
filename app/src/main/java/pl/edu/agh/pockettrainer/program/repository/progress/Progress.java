package pl.edu.agh.pockettrainer.program.repository.progress;

import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.ActionRecord;
import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.domain.time.TimeInstant;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;

public class Progress {

    private final ProgressRepository progressRepository;
    private final Program program;
    private final TrainingProgress trainingProgress;

    public Progress(MetaRepository metaRepository, Program program, TrainingProgress trainingProgress) {
        this.progressRepository = metaRepository.getProgressRepository();
        this.program = program;
        this.trainingProgress = trainingProgress;
    }

    public Program getProgram() {
        return program;
    }

    public TrainingProgress get() {
        return trainingProgress;
    }

    public ProgressState getState() {

        final int numRecords = trainingProgress.getNumRecords();

        if (numRecords == 0) {
            return ProgressState.NEW;
        }

        if (numRecords == program.getNumActions()) {
            return ProgressState.FINISHED;
        }

        if (hasUnfinishedDay(numRecords)) {
            return ProgressState.IN_PROGRESS;
        }

        final ActionRecord lastAction = trainingProgress.getLastRecord();
        final int numRecoveryDays = getNumRecoveryDaysAfter(numRecords);

        final TimeInstant previous = lastAction.startedAt;
        final TimeInstant nextPlanned = previous.plusDays(1 + numRecoveryDays);
        final TimeInstant deadline = nextPlanned.endOfDay().minus(lastAction.getDuration());
        final TimeInstant currentTime = TimeInstant.now();

        if (currentTime.isAfter(deadline)) {
            return ProgressState.BELATED;
        }

        if (currentTime.isAfter(nextPlanned)) {
            return ProgressState.READY;
        }

        return ProgressState.RECOVERY;
    }

    private boolean hasUnfinishedDay(int numRecords) {

        int cumulativeActions = 0;
        for (Day day : program.getSchedule()) {
            cumulativeActions += day.getNumActions();

            if (numRecords < cumulativeActions) {
                return true;
            } else if (numRecords == cumulativeActions) {
                return false;
            }
        }

        return false;
    }

    private int getNumRecoveryDaysAfter(int numRecords) {

        final List<Day> schedule = program.getSchedule();

        int cumulativeActions = 0;
        int dayIndex = 0;

        while (cumulativeActions < numRecords) {
            final Day day = schedule.get(dayIndex++);
            cumulativeActions += day.getNumActions();
        }

        int numRecoveryDays = 0;
        for (int i = dayIndex + 1; i < schedule.size(); i++) {
            final Day day = schedule.get(i);
            if (day.isRecovery()) {
                numRecoveryDays++;
            } else {
                break;
            }
        }

        return numRecoveryDays;
    }

    public TimeInstant getNextTrainingAt() {

        final int numRecords = trainingProgress.getNumRecords();

        final ActionRecord lastAction = trainingProgress.getLastRecord();
        final int numRecoveryDays = getNumRecoveryDaysAfter(numRecords);

        final TimeInstant previous = lastAction.startedAt;
        final TimeInstant nextPlanned = previous.plusDays(1 + numRecoveryDays);

        return nextPlanned;
    }

    public int getPercentage() {
        double percent = 100.0 * trainingProgress.getNumRecords() / program.getNumActions();
        return (int) percent;
    }

    public void startAction() {

        final TimeInstant now = TimeInstant.now();

        // TODO progress.startAction()
        progressRepository.update(this);
    }

    public void skipAction() {

        final TimeInstant now = TimeInstant.now();

        // TODO progress.skipAction()
        progressRepository.update(this);
    }

    public void finishAction() {

        final TimeInstant now = TimeInstant.now();

        // TODO progress.finishAction()
        progressRepository.update(this);
    }
}
