package pl.edu.agh.pockettrainer.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class TimedRecoveryActivity extends AppCompatActivity {

    // TODO TTS recovery time 30 seconds...
    // TODO TTS next exercise: "push-ups"
    // TODO destroy() shutdown TTS

    private TextView secondsLabel;

    @Override
    public void onBackPressed() {
        // prevent from interrupting timed recovery
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timed_recovery);

        final ApplicationState state = (ApplicationState) getApplicationContext();

        final TimedRecovery timedRecoveryAction = (TimedRecovery) state.action;
        secondsLabel  = findViewById(R.id.timed_recovery_seconds);
        secondsLabel.setText(String.valueOf(timedRecoveryAction.getSeconds()));

        final TextView comingLabel = findViewById(R.id.timed_recovery_coming_label);
        final ImageView imageView = findViewById(R.id.timed_recovery_next_image);
        final ImageView iconView = findViewById(R.id.timed_recovery_next_icon);
        final TextView title = findViewById(R.id.timed_recovery_next_title);
        final TextView label = findViewById(R.id.timed_recovery_next_label);

        if (state.futureAction != null) {

            comingLabel.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);

            if (state.futureAction instanceof TimedAction) {
                final TimedAction timedAction = (TimedAction) state.futureAction;
                final Exercise exercise = timedAction.getExercise();
                setImage(imageView, exercise.getImage());
                iconView.setImageResource(R.drawable.ic_watch);
                title.setText(capitalize(exercise.getName()));
                label.setText(timedAction.getSeconds() + " seconds");
            } else if (state.futureAction instanceof RepsAction) {
                final RepsAction repsAction = (RepsAction) state.futureAction;
                final Exercise exercise = repsAction.getExercise();
                setImage(imageView, exercise.getImage());
                iconView.setImageResource(R.drawable.ic_reps);
                title.setText(capitalize(exercise.getName()));
                label.setText(repsAction.getReps() + " reps");
            } else if (state.futureAction instanceof TimedRecovery) {
                final TimedRecovery timedRecovery = (TimedRecovery) state.futureAction;
                iconView.setImageResource(R.drawable.ic_watch);
                title.setText("Timed recovery");
                label.setText(timedRecovery.getSeconds() + " seconds");
            } else if (state.futureAction instanceof Recovery) {
                final Recovery recovery = (Recovery) state.futureAction;
                iconView.setImageResource(R.drawable.ic_reps);
                title.setText("Recovery");
                label.setText("");
            }
        } else {
            comingLabel.setVisibility(View.INVISIBLE);
            iconView.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            label.setVisibility(View.INVISIBLE);
        }
    }

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
