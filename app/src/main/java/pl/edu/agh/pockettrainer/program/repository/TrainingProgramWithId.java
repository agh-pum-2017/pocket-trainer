package pl.edu.agh.pockettrainer.program.repository;

import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;

public class TrainingProgramWithId {

    private final String id;
    private final TrainingProgram program;

    public TrainingProgramWithId(String id, TrainingProgram program) {
        this.id = id;
        this.program = program;
    }

    public String getId() {
        return id;
    }

    public TrainingProgram getProgram() {
        return program;
    }

    @Override
    public String toString() {
        return "TrainingProgramWithId{" +
                "id='" + id + '\'' +
                '}';
    }
}
