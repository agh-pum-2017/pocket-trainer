package pl.edu.agh.pockettrainer.program.domain.days;

public interface Day extends Comparable<Day> {

    String getName();

    boolean isRecovery();

    int getNumActions();
}
