package pl.edu.agh.pockettrainer.program.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.time.TimeInstant;

public class TrainingProgress {

    private final List<ActionRecord> records;

    public static TrainingProgress empty() {

        // TODO dummy test data
/*
        return new TrainingProgress(Arrays.asList(
            new ActionRecord(
                TimeInstant.fromString("2018-05-05T10:00:00.000"),
                TimeInstant.fromString("2018-05-05T10:01:00.000"),
                false),
            new ActionRecord(
                TimeInstant.fromString("2018-05-05T10:02:00.000"),
                TimeInstant.fromString("2018-05-05T10:03:00.000"),
                false),
            new ActionRecord(
                TimeInstant.fromString("2018-05-05T10:03:00.000"),
                TimeInstant.fromString("2018-05-05T10:04:00.000"),
                false),
            new ActionRecord(
                TimeInstant.fromString("2018-05-05T10:05:00.000"),
                TimeInstant.fromString("2018-05-05T10:06:00.000"),
                false),
            new ActionRecord(
                TimeInstant.fromString("2018-05-05T10:07:00.000"),
                TimeInstant.fromString("2018-05-05T10:08:00.000"),
                false),
        ));
*/
        return new TrainingProgress(new ArrayList<ActionRecord>());
    }

    public TrainingProgress(List<ActionRecord> records) {
        this.records = records;
    }

    public List<ActionRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public int getNumRecords() {
        return records.size();
    }

    public ActionRecord getLastRecord() {
        return records.get(getLastIndex());
    }

    private int getLastIndex() {
        return records.size() - 1;
    }
}
