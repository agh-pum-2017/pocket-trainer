package pl.edu.agh.pockettrainer.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;

/**
 * Created by Mateusz on 5/4/2018.
 */

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    public ExerciseAdapter(@NonNull Context context, List<Exercise> exerciseList) {
        super(context,0, exerciseList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Exercise exercise = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_item, parent, false);
        }

        TextView exerciseName = convertView.findViewById(R.id.textViewExerciseName);
        TextView exerciseDescription = convertView.findViewById(R.id.textViewExerciseDescription);
        exerciseName.setText(exercise.getName());
        exerciseDescription.setText(exercise.getDescription());
        ImageView imageView = convertView.findViewById(R.id.imageTop);
        ImageView imageMuscles = convertView.findViewById(R.id.imageMuscles);
        Glide.with(convertView).load(exercise.getImage()).into(imageView);
        Glide.with(convertView).load(exercise.getImageOfMuslces()).into(imageMuscles);

        return convertView;
    }
}
