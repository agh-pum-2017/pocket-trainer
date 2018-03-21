package pl.edu.agh.pockettrainer.program.domain.actions;

import pl.edu.agh.pockettrainer.program.domain.Exercise;

public class RepsAction implements Action {

    private final Exercise exercise;
    private final int reps;

    public RepsAction(Exercise exercise, int reps) {
        this.exercise = exercise;
        this.reps = reps;
    }

    @Override
    public boolean isRecovery() {
        return false;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getReps() {
        return reps;
    }

    @Override
    public String toString() {
        return "RepsAction{" +
                "exercise=" + exercise +
                ", reps=" + reps +
                '}';
    }
}
