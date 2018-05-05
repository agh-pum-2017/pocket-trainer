package pl.edu.agh.pockettrainer.program.domain;

import pl.edu.agh.pockettrainer.program.domain.time.TimeDuration;
import pl.edu.agh.pockettrainer.program.domain.time.TimeInstant;

public class ActionRecord {

    public final TimeInstant startedAt;
    public final TimeInstant finishedAt;
    public final boolean skipped;

    public ActionRecord(TimeInstant startedAt, TimeInstant finishedAt, boolean skipped) {
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.skipped = skipped;
    }

    public TimeDuration getDuration() {
        return TimeDuration.of(startedAt, finishedAt);
    }
}
