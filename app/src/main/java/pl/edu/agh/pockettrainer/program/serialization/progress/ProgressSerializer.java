package pl.edu.agh.pockettrainer.program.serialization.progress;

import pl.edu.agh.pockettrainer.program.domain.ActionRecord;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;

public class ProgressSerializer {

    public String serialize(TrainingProgress progress) {

        final StringBuilder sb = new StringBuilder();

        for (ActionRecord record : progress.getRecords()) {
            sb.append(record.startedAt)
              .append(",")
              .append(record.finishedAt)
              .append(",")
              .append(record.skipped)
              .append("\n");
        }

        return sb.toString();
    }
}
