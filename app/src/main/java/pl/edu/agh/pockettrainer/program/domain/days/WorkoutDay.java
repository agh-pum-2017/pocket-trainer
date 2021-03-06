package pl.edu.agh.pockettrainer.program.domain.days;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.actions.Action;

public class WorkoutDay implements Day {

    private final String name;
    private final List<Action> routine;

    public WorkoutDay(String name, List<Action> routine) {
        this.name = name;
        this.routine = routine;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isRecovery() {
        return false;
    }

    @Override
    public int getNumActions() {
        return routine.size(); // including recovery breaks
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(routine);
    }

    @Override
    public String toString() {
        return "WorkoutDay{" +
                "routine=" + routine +
                '}';
    }

    @Override
    public int compareTo(@NonNull Day other) {
        return name.compareTo(other.getName());
    }
}
