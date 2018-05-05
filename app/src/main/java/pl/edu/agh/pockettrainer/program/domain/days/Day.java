package pl.edu.agh.pockettrainer.program.domain.days;

import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.actions.Action;

public interface Day extends Comparable<Day> {

    String getName();

    boolean isRecovery();

    int getNumActions();

    List<Action> getActions();
}
