package pl.edu.agh.pockettrainer.program.domain.days;

import android.support.annotation.NonNull;

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
    public String toString() {
        return "RunningDay{}";
    }

    @Override
    public int compareTo(@NonNull Day other) {
        return name.compareTo(other.getName());
    }
}
