package pl.edu.agh.pockettrainer.program.repository.program;

import java.util.Iterator;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.days.Day;

public class ActionIterator implements Iterator<Action> {

    private final List<Day> schedule;

    private int dayIndex = 0;
    private int actionIndex = 0;

    public ActionIterator(List<Day> schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean hasNext() {

        int di = dayIndex;
        int ai = actionIndex;

        while (di < schedule.size()) {
            final Day day = schedule.get(di);
            if (day.isRecovery()) {
                di++;
            } else {
                if (ai < day.getNumActions()) {
                    return true;
                } else {
                    di++;
                    ai = 0;
                }
            }
        }
        return false;
    }

    @Override
    public Action next() {
        while (dayIndex < schedule.size()) {
            final Day day = schedule.get(dayIndex);
            if (day.isRecovery()) {
                dayIndex++;
            } else {
                if (actionIndex < day.getNumActions()) {
                    final Action action = day.getActions().get(actionIndex);
                    actionIndex++;
                    return action;
                } else {
                    dayIndex++;
                    actionIndex = 0;
                }
            }
        }
        return null;
    }
}
