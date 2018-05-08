package pl.edu.agh.pockettrainer.program.domain;

import pl.edu.agh.pockettrainer.program.domain.actions.Action;

public class TrackedActionRecord extends ActionRecord {

    public final Action action;

    public TrackedActionRecord(Action action, ActionRecord record) {
        super(record.startedAt, record.finishedAt, record.skipped);
        this.action = action;
    }
}
