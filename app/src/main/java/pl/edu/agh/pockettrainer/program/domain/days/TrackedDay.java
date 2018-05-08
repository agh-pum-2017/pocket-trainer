package pl.edu.agh.pockettrainer.program.domain.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.TrackedActionRecord;
import pl.edu.agh.pockettrainer.program.domain.time.TimeDuration;

public class TrackedDay {

    private final int index;
    private List<TrackedActionRecord> trackedRecords;

    public TrackedDay(int index) {
        this.index = index;
        this.trackedRecords = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public int getNumber() {
        return index + 1;
    }

    public List<TrackedActionRecord> getRecords() {
        return Collections.unmodifiableList(trackedRecords);
    }

    public void add(TrackedActionRecord record) {
        trackedRecords.add(record);
    }

    public TimeDuration getTotalTime() {
        final TrackedActionRecord first = trackedRecords.get(0);
        final TrackedActionRecord last = trackedRecords.get(trackedRecords.size() - 1);
        return last.finishedAt.minus(first.startedAt);
    }
}
