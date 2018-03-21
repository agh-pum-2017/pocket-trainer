package pl.edu.agh.pockettrainer.program.domain.actions;

import pl.edu.agh.pockettrainer.program.domain.Exercise;

public class TimedAction implements Action {

    private final Exercise exercise;
    private final int seconds;

    public TimedAction(Exercise exercise, int seconds) {
        this.exercise = exercise;
        this.seconds = seconds;
    }

    @Override
    public boolean isRecovery() {
        return false;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return "TimedAction{" +
                "exercise=" + exercise +
                ", seconds=" + seconds +
                '}';
    }
}
