package pl.edu.agh.pockettrainer.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.TrackedActionRecord;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.days.TrackedDay;
import pl.edu.agh.pockettrainer.program.domain.time.TimeDuration;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;

public class HistoryAdapter extends ArrayAdapter<TrackedDay> {

    public HistoryAdapter(Context context, Progress progress) {
        super(context, 0, progress.getTrackedDays());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final TrackedDay trackedDay = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        final TextView title = convertView.findViewById(R.id.history_item_title);
        title.setText("Day " + trackedDay.getNumber());

        final TextView startedAtLabel = convertView.findViewById(R.id.history_item_started);
        final TrackedActionRecord dayFirstRecord = trackedDay.getRecords().get(0);
        startedAtLabel.setText(dayFirstRecord.startedAt.toString() + " UTC");

        final TextView totalTime = convertView.findViewById(R.id.history_item_total_time);
        totalTime.setText(trackedDay.getTotalTime().toString());

        final TextView content = convertView.findViewById(R.id.history_item_content);
        content.setText(getContent(trackedDay));

        return convertView;
    }

    private String getContent(TrackedDay trackedDay) {

        final StringBuilder sb= new StringBuilder();
        final List<TrackedActionRecord> records = trackedDay.getRecords();
        for (int i = 0; i < records.size(); i++) {

            final TrackedActionRecord record = records.get(i);

            final String status = record.skipped ? "✗ " : "✔ ";

            if (record.action.isRecovery()) {
                sb.append(status).append("Recovery");
            } else {
                final Exercise exercise = getExercise(record.action);
                sb.append(status).append(capitalize(exercise.getName()));
            }

            if (!record.skipped) {
                sb.append(" (").append(record.getDuration()).append(")");
            }

            if (i < records.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private String capitalize(String string) {
        string = string.toLowerCase().replaceAll("[_-]", " ");
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private Exercise getExercise(Action action) {
        if (action instanceof TimedAction) {
            final TimedAction timedAction = (TimedAction) action;
            return timedAction.getExercise();
        } else if (action instanceof RepsAction) {
            final RepsAction repsAction = (RepsAction) action;
            return repsAction.getExercise();
        }
        return null;
    }
}
