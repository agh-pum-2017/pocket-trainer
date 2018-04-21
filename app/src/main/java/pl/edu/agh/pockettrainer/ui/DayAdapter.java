package pl.edu.agh.pockettrainer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.ui.activities.ProgramDetailsActivity;

public class DayAdapter extends ArrayAdapter<Day> {

    public DayAdapter(@NonNull Context context, List<Day> dayList) {
        super(context, 0, dayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("SWP","[DayAdapter] getView(...), position = "+Integer.toString(position));
        Day day = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.day_item, parent, false);
        }

        TextView DayLabel = convertView.findViewById(R.id.day_label);
        TextView ExercisesLabel = convertView.findViewById(R.id.exercise_list_label);

        DayLabel.setText("Day " + Integer.toString(position+1) + "");
        ExercisesLabel.setText(day.getName() + ": " + "list of exercises");

        //convertView.setOnClickListener(onClick(day));
        Log.d("SWP","[DayAdapter] day.getName() = "+day.getName());
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
        List<DecoratedProgram> programList = programs.getInstalled();
        for(int i=0;i<programList.size();i++){
            if(program.getId()==programList.get(i).getId())
                intent.putExtra("position", i);
        }
        context.startActivity(intent);*/
    }
}
