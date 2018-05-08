package pl.edu.agh.pockettrainer.program.repository.program.iterator;

import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;

public class PointedAction {

    public final Action action;
    public final Pointer pointer;

    public PointedAction(Action action, Pointer pointer) {
        this.action = action;
        this.pointer = pointer;
    }

    public boolean isRecovery() {
        return action.isRecovery();
    }

    public boolean isRecoveryAction() {
        return action instanceof Recovery;
    }

    public boolean isTimedRecoveryAction() {
        return action instanceof TimedRecovery;
    }

    public boolean isTimedAction() {
        return action instanceof TimedAction;
    }

    public boolean isRepsAction() {
        return action instanceof RepsAction;
    }

    public boolean isSameDayAs(PointedAction action) {
        return pointer.dayIndex == action.pointer.dayIndex;
    }
}
