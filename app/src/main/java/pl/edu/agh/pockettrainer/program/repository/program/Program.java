package pl.edu.agh.pockettrainer.program.repository.program;

import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.iterator.ActionIterator;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.program.repository.progress.ProgressRepository;

public class Program {

    private final ProgramRepository programRepository;
    private final ProgressRepository progressRepository;
    private final String id;
    private final TrainingProgram program;
    private final int numActions;

    public Program(MetaRepository metaRepository, String id, TrainingProgram program) {
        this.programRepository = metaRepository.getProgramRepository();
        this.progressRepository = metaRepository.getProgressRepository();
        this.id = id;
        this.program = program;
        this.numActions = calculateNumActions(program);
    }

    @Override
    public String toString() {
        return "Program{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public TrainingProgram get() {
        return program;
    }

    public Progress getProgress() {
        return progressRepository.getProgress(this);
    }

    public Metadata getMetadata() {
        return program.getMetadata();
    }

    public Set<Exercise> getExercises() {
        return program.getExercises();
    }

    public List<Day> getSchedule() {
        return program.getSchedule();
    }

    public ActionIterator getActionIterator() {
        return new ActionIterator(program.getSchedule());
    }

    public boolean isActive() {
        final Program activeProgram = programRepository.getActiveProgram();
        return activeProgram != null && id.equals(activeProgram.id);
    }

    public void setActive() {
        programRepository.setActiveProgram(this);
    }

    public void setInactive() {
        programRepository.unsetActiveProgram();
        progressRepository.deleteProgress(this);
    }

    public void toggleActive() {
        if (isActive()) {
            setInactive();
        } else {
            progressRepository.deleteAll();
            setActive();
        }
    }

    public int getNumActions() {
        return numActions;
    }

    private int calculateNumActions(TrainingProgram program) {
        int numActions = 0;
        for (Day day : program.getSchedule()) {
            numActions += day.getNumActions();
        }
        return numActions;
    }
}
