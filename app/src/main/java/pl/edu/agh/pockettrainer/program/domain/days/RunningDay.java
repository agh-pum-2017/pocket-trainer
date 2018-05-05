package pl.edu.agh.pockettrainer.program.domain.days;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.actions.Action;

public class RunningDay implements Day {

    private final String name;

    public RunningDay(String name) {
        this.name = name;
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
        return 1;
    }

    @Override
    public List<Action> getActions() {
        final Action runningAction = new Action() {

            // TODO two types: timed vs. distance

            @Override
            public boolean isRecovery() {
                return false;
            }
        };
        return Collections.singletonList(runningAction);
    }

    @Override
    public String toString() {
        return "RunningDay{}";
    }

    @Override
    public int compareTo(@NonNull Day other) {
        return name.compareTo(other.getName());
    }
}
