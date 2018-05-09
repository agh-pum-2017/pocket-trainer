package pl.edu.agh.pockettrainer.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.ui.DownloadableProgram;

public class DownloadableProgramAdapter extends ArrayAdapter<DownloadableProgram> {

    public DownloadableProgramAdapter(Context context, List<DownloadableProgram> programs) {
        super(context, 0, programs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DownloadableProgram program = getItem(position);
        final Metadata metadata = program.metadata;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.downloadable_program_item, parent, false);
        }

        final ImageView imageView = convertView.findViewById(R.id.downloadable_program_item_image);
        Glide.with(imageView).load(program.getThumbnail()).into(imageView);



        // TODO encoded image setImage(convertView, imageView, metadata);

        final TextView title = convertView.findViewById(R.id.downloadable_program_item_title);
        title.setText(metadata.getName());

        final TextView description = convertView.findViewById(R.id.downloadable_program_item_description);
        description.setText(metadata.getDescription());

        final TextView goals = convertView.findViewById(R.id.downloadable_program_item_goals);
        goals.setText(makeString(metadata.getGoals()));


//        final Button buttonToggleEnroll = convertView.findViewById(R.id.downloadable_program_item_button_enroll);
//        final Button buttonDelete = convertView.findViewById(R.id.downloadable_program_item_button_delete);
//
//
//

//
//        convertView.setOnClickListener(onClick(program));
//        buttonToggleEnroll.setOnClickListener(onBtnToggleEnrollClick(program));
//        buttonDelete.setOnClickListener(onBtnDeleteClick(program));


        return convertView;
    }

    private void setImage(View view, ImageView imageView, Metadata metadata) {
        Glide.with(view).load(metadata.getImage()).into(imageView);
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
