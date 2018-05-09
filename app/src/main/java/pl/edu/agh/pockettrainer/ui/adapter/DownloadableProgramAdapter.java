package pl.edu.agh.pockettrainer.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.ui.DownloadableProgram;
import pl.edu.agh.pockettrainer.ui.components.CustomCheckBox;

public class DownloadableProgramAdapter extends ArrayAdapter<DownloadableProgram> {

    private Set<DownloadableProgram> selectedPrograms = new HashSet<>();
    private final int programCount;

    public DownloadableProgramAdapter(Context context, List<DownloadableProgram> programs) {
        super(context, 0, programs);
        this.programCount = programs.size();
    }

    public Set<DownloadableProgram> getSelectedPrograms() {
        return selectedPrograms;
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

        final TextView title = convertView.findViewById(R.id.downloadable_program_item_title);
        title.setText(metadata.getName());

        final TextView description = convertView.findViewById(R.id.downloadable_program_item_description);
        description.setText(metadata.getDescription());

        final TextView goals = convertView.findViewById(R.id.downloadable_program_item_goals);
        goals.setText(makeString(metadata.getGoals()));

        final CheckBox checkBox = convertView.findViewById(R.id.downloadable_program_item_checkbox);
        checkBox.setOnCheckedChangeListener(onCheck(convertView, program));

        convertView.setOnClickListener(onClick());

        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener onCheck(final View convertView, final DownloadableProgram program) {
        return new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    selectedPrograms.add(program);
                } else {
                    selectedPrograms.remove(program);
                }

                final View parent = (View) convertView.getParent().getParent();
                final TextView countLabel = parent.findViewById(R.id.download_selected_count);
                countLabel.setText("Selected " + selectedPrograms.size());

                final CustomCheckBox selectAll = parent.findViewById(R.id.download_select_all);
                selectAll.setChecked(selectedPrograms.size() == programCount, false);
            }
        };
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

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final CheckBox checkBox = view.findViewById(R.id.downloadable_program_item_checkbox);
                checkBox.toggle();
            }
        };
    }
}
