package pl.edu.agh.pockettrainer.program.domain.days;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.actions.Action;

public class RecoveryDay implements Day {

    private final String name;

    public RecoveryDay(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isRecovery() {
        return true;
    }

    @Override
    public int getNumActions() {
        return 0;
    }

    @Override
    public List<Action> getActions() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "RecoveryDay{}";
    }

    @Override
    public int compareTo(@NonNull Day other) {
        return name.compareTo(other.getName());
    }
}
