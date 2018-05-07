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

import java.io.File;
import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    private final List<Exercise> exercises;

    public ExerciseAdapter(@NonNull Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Exercise exercise = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_item, parent, false);
        }

        final ImageView imageView = convertView.findViewById(R.id.program_details_exercise_image);
        final TextView title = convertView.findViewById(R.id.program_details_exercise_title);
        final TextView description = convertView.findViewById(R.id.program_details_exercise_description);

        setImage(convertView, imageView, exercise.getImage());
        title.setText(capitalize(exercise.getName()));
        description.setText(exercise.getDescription());

        return convertView;
    }

    private void setImage(View view, ImageView imageView, File imageFile) {
        Glide.with(view).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
