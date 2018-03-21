package pl.edu.agh.pockettrainer.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.repository.DecoratedProgram;

public class ProgramAdapter extends ArrayAdapter<DecoratedProgram> {

    public ProgramAdapter(@NonNull Context context, List<DecoratedProgram> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DecoratedProgram program = getItem(position);
        final Metadata metadata = program.getMetadata();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.program_item, parent, false);
        }

        final TextView title = convertView.findViewById(R.id.label_title);
        final TextView goals = convertView.findViewById(R.id.label_goals);
        final ImageView imageView = convertView.findViewById(R.id.program_image);

        title.setText(metadata.getName());
        goals.setText(makeString(metadata.getGoals()));
        imageView.setImageBitmap(getImage(metadata));

        return convertView;
    }

    private String makeString(Set<ProgramGoal> goals) {
        final List<String> strings = new ArrayList<>();
        for (ProgramGoal goal : goals) {
            strings.add(goal.name().toLowerCase().replaceAll("_", " "));
        }
        Collections.sort(strings);
        String result = strings.toString();
        return result.substring(1, result.length() - 1);
    }

    private Bitmap getImage(Metadata metadata) {
        final File file = metadata.getImage();
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }
}
