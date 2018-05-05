package pl.edu.agh.pockettrainer.program.serialization.progress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.ActionRecord;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;
import pl.edu.agh.pockettrainer.program.domain.time.TimeInstant;

public class ProgressDeserializer {

    public TrainingProgress parse(String csv) {

        final List<ActionRecord> records = new ArrayList<>();

        for (String line : csv.split("\\n")) {
            if (!line.trim().startsWith("#")) {
                List<String> columns = Arrays.asList(line.split(","));
                if (columns.size() == 3) {

                    TimeInstant startedAt = TimeInstant.fromString(columns.get(0));
                    TimeInstant finishedAt = TimeInstant.fromString(columns.get(1));
                    boolean skipped = Boolean.parseBoolean(columns.get(2));

                    records.add(new ActionRecord(startedAt, finishedAt, skipped));
                }
            }
        }

        return new TrainingProgress(records);
    }
}
