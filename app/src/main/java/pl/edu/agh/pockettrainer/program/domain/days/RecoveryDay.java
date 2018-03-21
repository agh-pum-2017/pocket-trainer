package pl.edu.agh.pockettrainer.program.domain.days;

import android.support.annotation.NonNull;

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
    public String toString() {
        return "RecoveryDay{}";
    }

    @Override
    public int compareTo(@NonNull Day other) {
        return name.compareTo(other.getName());
    }
}
