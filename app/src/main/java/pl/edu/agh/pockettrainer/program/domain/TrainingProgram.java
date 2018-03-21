package pl.edu.agh.pockettrainer.program.domain;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.program.domain.days.Day;

public class TrainingProgram {

    private final Metadata metadata;
    private final Set<Exercise> exercises;
    private final List<Day> schedule;

    public TrainingProgram(Metadata metadata, Set<Exercise> exercises, List<Day> schedule) {
        this.metadata = metadata;
        this.exercises = exercises;
        this.schedule = schedule;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Set<Exercise> getExercises() {
        return Collections.unmodifiableSet(exercises);
    }

    public List<Day> getSchedule() {
        return Collections.unmodifiableList(schedule);
    }

    @Override
    public String toString() {
        return "TrainingProgram{" +
                "metadata=" + metadata +
                ", exercises=" + exercises +
                ", schedule=" + schedule +
                '}';
    }
}
