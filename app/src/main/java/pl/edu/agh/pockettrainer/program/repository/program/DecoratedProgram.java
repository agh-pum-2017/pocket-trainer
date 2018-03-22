package pl.edu.agh.pockettrainer.program.repository.program;

import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;
import pl.edu.agh.pockettrainer.program.domain.days.Day;

public class DecoratedProgram {

    private final ProgramRepository repository;
    private final String id;
    private final TrainingProgram program;

    public DecoratedProgram(ProgramRepository repository, String id, TrainingProgram program) {
        this.repository = repository;
        this.id = id;
        this.program = program;
    }

    @Override
    public String toString() {
        return "DecoratedProgram{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public TrainingProgram get() {
        return program;
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

    public boolean isActive() {
        final DecoratedProgram activeProgram = repository.getActiveProgram();
        return activeProgram != null && id.equals(activeProgram.id);
    }

    public void setActive() {
        repository.setActiveProgram(this);
    }

    public void setInactive() {
        repository.unsetActiveProgram();
    }

    public void toggleActive() {
        if (isActive()) {
            setInactive();
        } else {
            setActive();
        }
    }
}
