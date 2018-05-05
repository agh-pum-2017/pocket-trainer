package pl.edu.agh.pockettrainer.program.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingProgress {

    private final List<ActionRecord> records;

    public static TrainingProgress empty() {
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

    public void addRecord(ActionRecord record) {
        records.add(record);
    }

    private int getLastIndex() {
        return records.size() - 1;
    }
}
