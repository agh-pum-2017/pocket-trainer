package pl.edu.agh.pockettrainer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.days.Day;

public class DayAdapter extends ArrayAdapter<Day> {

    public DayAdapter(@NonNull Context context, List<Day> dayList) {
        super(context, 0, dayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Day day = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.day_item, parent, false);
        }

        TextView DayLabel = convertView.findViewById(R.id.day_label);
        TextView ExercisesLabel = convertView.findViewById(R.id.exercise_list_label);

        DayLabel.setText("Day " + Integer.toString(position+1) + "");
        ExercisesLabel.setText(day.getName() + ": " + "list of exercises : " + day.toString());

        return convertView;
    }

    private View.OnClickListener onClick(final Day day) {
        return new SingleClickListener() {

            @Override
            public void performClick(View view) {
                //navigateTo(WorkoutActivity.class, program);
            }
        };
    }

    private void navigateTo(Class<? extends Activity> activityClass, Day day) {
        final Context context = getContext();
        final Intent intent = new Intent(context, activityClass);
        /*intent.putExtra("programId", program.getId());
        context.startActivity(intent);*/
    }
}
