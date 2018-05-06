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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;

/**
 * Created by Mateusz on 5/6/2018.
 */

public class ProgramDetailsAdapter extends ArrayAdapter<DecoratedProgram> {
    public ProgramDetailsAdapter(@NonNull Context context, List<DecoratedProgram> programList) {
        super(context,0, programList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DecoratedProgram program = getItem(position);
        Metadata metadata = program.getMetadata();
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.program_details_item,parent,false);//rename layout to program_details_item
        }
        final ImageView imageView = convertView.findViewById(R.id.program_image);
        final ImageView genderView = convertView.findViewById(R.id.gender_image);
        final TextView title = convertView.findViewById(R.id.label_title);
        final TextView goals = convertView.findViewById(R.id.label_goals);
        final TextView description = convertView.findViewById(R.id.label_description);
        final TextView author = convertView.findViewById(R.id.label_author);
        if(metadata!=null) {
            File image = metadata.getImage();
            if(image!=null)
                Glide.with(convertView).load(image).into(imageView);
            title.setText(metadata.getName());
            goals.setText(makeString(metadata.getGoals()));
            description.setText(metadata.getDescription());
            author.setText(metadata.getAuthor());
            switch (metadata.getTargetGender()){
                case ANY:
                    Glide.with(convertView).load(R.drawable.gender_any).into(genderView);
                    break;
                case FEMALE:
                    Glide.with(convertView).load(R.drawable.gender_female).into(genderView);
                    break;
                case MALE:
                    Glide.with(convertView).load(R.drawable.gender_male).into(genderView);
                    break;
            }
        }

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
}
