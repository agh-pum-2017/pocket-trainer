package pl.edu.agh.pockettrainer.program.repository.progress;

import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.ActionRecord;
import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.domain.time.TimeInstant;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.iterator.ActionIterator;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.iterator.PointedAction;

public class Progress {

    private final ProgressRepository progressRepository;
    private final Program program;
    private final TrainingProgress trainingProgress;

    private TimeInstant startedAt;

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

        if (numRecords <= 0) {
            return ProgressState.NEW;
        }

        if (numRecords >= program.getNumActions()) {
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
        startedAt = TimeInstant.now();
    }

    public void skipAction() {

        if (startedAt != null) {

            final TimeInstant skippedAt = TimeInstant.now();
            final ActionRecord record = new ActionRecord(startedAt, skippedAt, true);
            trainingProgress.addRecord(record);

            progressRepository.update(this);

            startedAt = null;
        }
    }

    public void finishAction() {

        if (startedAt != null) {

            final TimeInstant finishedAt = TimeInstant.now();
            final ActionRecord record = new ActionRecord(startedAt, finishedAt, true);
            trainingProgress.addRecord(record);

            progressRepository.update(this);

            startedAt = null;
        }
    }

    public PointedAction getNextAction() {

        final ActionIterator it = program.getActionIterator();

        for (int i = 0; it.hasNext() && i < trainingProgress.getNumRecords(); i++) {
            it.next();
        }

        return it.hasNext() ? it.next() : null;
    }

    public PointedAction getFutureAction() {

        final PointedAction currentAction = getNextAction();

        final ActionIterator it = program.getActionIterator();

        for (int i = 0; it.hasNext() && i < trainingProgress.getNumRecords() + 1; i++) {
            it.next();
        }

        if (it.hasNext()) {
            PointedAction futureAction = it.next();
            if (futureAction.isSameDayAs(currentAction)) {
                return futureAction;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void delete() {
        progressRepository.deleteProgress(program);
    }
}
