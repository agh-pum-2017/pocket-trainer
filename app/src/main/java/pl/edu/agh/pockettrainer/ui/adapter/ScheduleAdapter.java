package pl.edu.agh.pockettrainer.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.days.Day;

public class ScheduleAdapter extends ArrayAdapter<Day> {

    private final List<Day> schedule;

    public ScheduleAdapter(Context context, List<Day> schedule) {
        super(context, 0, schedule);
        this.schedule = schedule;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Day day = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_item, parent, false);
        }

        final TextView title = convertView.findViewById(R.id.program_details_schedule_title);
        final TextView description = convertView.findViewById(R.id.program_details_schedule_description);

        title.setText("Day " + (position + 1));

        if (day.isRecovery()) {
            convertView.setBackground(new ColorDrawable(Color.parseColor("#cccccc")));
            description.setText("Recovery");
        } else{
            convertView.setBackground(new ColorDrawable(Color.parseColor("#eeeeee")));
            description.setText(getDescription(day));
        }

        return convertView;
    }

    private String getDescription(Day day) {

        final Map<Exercise, Integer> map = getExerciseCount(day);
        final List<Exercise> exercises = sorted(map.keySet());

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exercises.size(); i++) {

            final Exercise exercise = exercises.get(i);
            final Integer count = map.get(exercise);

            sb.append(count).append("× ").append(exercise.getName().toLowerCase());

            if (i < exercises.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private Map<Exercise, Integer> getExerciseCount(Day day) {
        final Map<Exercise, Integer> map = new HashMap<>();
        for (Action action : day.getActions()) {
            if (action instanceof TimedAction) {
                final TimedAction timedAction = (TimedAction) action;
                final Exercise exercise = timedAction.getExercise();
                if (!map.containsKey(exercise)) {
                    map.put(exercise, 0);
                }
                map.put(exercise, map.get(exercise) + 1);
            } else if (action instanceof RepsAction) {
                final RepsAction repsAction = (RepsAction) action;
                final Exercise exercise = repsAction.getExercise();
                if (!map.containsKey(exercise)) {
                    map.put(exercise, 0);
                }
                map.put(exercise, map.get(exercise) + 1);
            }
        }
        return map;
    }

    private List<Exercise> sorted(Set<Exercise> exercises) {

        final List<Exercise> sorted = new ArrayList<>(exercises);

        Collections.sort(sorted, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise o1, Exercise o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return sorted;
    }
}
