package pl.edu.agh.pockettrainer.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.Muscle;

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
        final ImageView musclesView = convertView.findViewById(R.id.program_details_exercise_muscles);

        setMuscles(musclesView, exercise.getMuscles());
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

    private void setMuscles(ImageView musclesView, Set<Muscle> muscles) {

        final List<Drawable> layers = new ArrayList<>();

        final Resources resources = getContext().getResources();
        layers.add(resources.getDrawable(R.drawable.m_figures));

        for (Muscle muscle : muscles) {
            switch (muscle) {
                case BACK_LOWER:
                    layers.add(resources.getDrawable(R.drawable.m_lower_back));
                    break;
                case BICEPS:
                    layers.add(resources.getDrawable(R.drawable.m_biceps));
                    break;
                case CALVES:
                    layers.add(resources.getDrawable(R.drawable.m_calves));
                    break;
                case DELTOIDS:
                    layers.add(resources.getDrawable(R.drawable.m_deltoids));
                    break;
                case FOREARMS:
                    layers.add(resources.getDrawable(R.drawable.m_forearms));
                    break;
                case GLUTES:
                    layers.add(resources.getDrawable(R.drawable.m_glutes));
                    break;
                case HAMSTRINGS:
                    layers.add(resources.getDrawable(R.drawable.m_hamstrings));
                    break;
                case LATS:
                    layers.add(resources.getDrawable(R.drawable.m_lats));
                    break;
                case OBLIQUES:
                    layers.add(resources.getDrawable(R.drawable.m_obliques));
                    break;
                case PECS:
                    layers.add(resources.getDrawable(R.drawable.m_pecs));
                    break;
                case QUADS:
                    layers.add(resources.getDrawable(R.drawable.m_quads));
                    break;
                case TRAPS:
                    layers.add(resources.getDrawable(R.drawable.m_traps));
                    break;
                case TRICEPS:
                    layers.add(resources.getDrawable(R.drawable.m_traps));
                    break;
            }
        }

        final LayerDrawable layerDrawable = new LayerDrawable(layers.toArray(new Drawable[]{}));
        musclesView.setImageDrawable(layerDrawable);
    }
}
