package pl.edu.agh.pockettrainer.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.SingleClickListener;
import pl.edu.agh.pockettrainer.ui.activities.ProgramDetailsActivity;

public class ProgramAdapter extends ArrayAdapter<Program> {

    private final ProgramRepository programs;

    public ProgramAdapter(@NonNull Context context, ProgramRepository programs) {
        super(context, 0, programs.getInstalled());
        this.programs = programs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Program program = getItem(position);
        final Metadata metadata = program.getMetadata();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.program_item, parent, false);
        }

        final ImageView imageView = convertView.findViewById(R.id.program_item_image);
        final TextView title = convertView.findViewById(R.id.program_item_title);
        final TextView goals = convertView.findViewById(R.id.program_item_goals);
        final Button buttonToggleEnroll = convertView.findViewById(R.id.program_item_button_enroll);
        final Button buttonDelete = convertView.findViewById(R.id.program_item_button_delete);

        title.setText(metadata.getName());
        goals.setText(makeString(metadata.getGoals()));
        setImage(convertView, imageView, metadata);

        convertView.setOnClickListener(onClick(program));
        buttonToggleEnroll.setOnClickListener(onBtnToggleEnrollClick(program));
        buttonDelete.setOnClickListener(onBtnDeleteClick(program));

        if (program.isActive()) {
            buttonToggleEnroll.setTextColor(0xff00a6ff);
            buttonToggleEnroll.setText("enrolled");
        } else {
            buttonToggleEnroll.setTextColor(0xffaaaaaa);
            buttonToggleEnroll.setText("enroll");
        }

        return convertView;
    }

    private View.OnClickListener onBtnDeleteClick(final Program program) {
        final Context context = getContext();
        final ApplicationState state = (ApplicationState) context.getApplicationContext();
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                program.uninstall();
                                state.programRepository.forceReload();
                                state.progressRepository.deleteProgress(program);

                                notifyDataSetInvalidated();

                                String message = "Deleted 1 training program";
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                String message = "Are you sure to delete this program?";
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message)
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
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

    private void setImage(View view, ImageView imageView, Metadata metadata) {
        Glide.with(view).load(metadata.getImage()).into(imageView);
    }

    private View.OnClickListener onClick(final Program program) {
        return new SingleClickListener() {

            @Override
            public void performClick(View view) {
                navigateTo(ProgramDetailsActivity.class, program);
            }
        };
    }

    private View.OnClickListener onBtnToggleEnrollClick(final Program program) {
        return new SingleClickListener() {

            @Override
            public void performClick(View view) {
                if (programs.hasActiveProgram()) {
                    final String message;
                    final String toastMessage;
                    if (program.isActive()) {
                        message = "This will erase your progress. Continue?";
                        toastMessage = null;
                    } else {
                        message = "You are already enrolled to another program. This will erase you progress. Continue?";
                        toastMessage = "Training program changed";
                    }
                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    program.toggleActive();
                                    notifyDataSetChanged();
                                    if (toastMessage != null) {
                                        Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(message)
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                } else {
                    program.setActive();
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "You are now enrolled", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void navigateTo(Class<? extends Activity> activityClass, Program program) {
        final Context context = getContext();
        final Intent intent = new Intent(context, activityClass);
        intent.putExtra("programId", program.getId());
        context.startActivity(intent);
    }
}
