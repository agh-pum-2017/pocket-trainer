package pl.edu.agh.pockettrainer.ui.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.program.repository.program.iterator.PointedAction;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class UpcomingActivity extends AppCompatActivity {

    private ApplicationState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        setTitle(Html.fromHtml("<font color='#ffffff'>Coming next...</font>"));

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00A6FF")));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_white);
        }

        state = (ApplicationState) getApplicationContext();

        final Progress progress = state.getProgress();
        final PointedAction pointedAction = progress.getNextAction();

        state.pointedAction = pointedAction;

        if (pointedAction.isRecovery()) {
            state.futurePointedAction = progress.getFutureAction();
        }

        final ImageView imageView = findViewById(R.id.upcoming_action_image);
        final ImageView iconView = findViewById(R.id.upcoming_action_icon);
        final TextView title = findViewById(R.id.upcoming_action_title);
        final TextView label = findViewById(R.id.upcoming_action_label);

        if (pointedAction.isTimedAction()) {
            final TimedAction timedAction = (TimedAction) pointedAction.action;
            final Exercise exercise = timedAction.getExercise();
            setImage(imageView, exercise.getImage());
            iconView.setImageResource(R.drawable.ic_watch);
            title.setText(capitalize(exercise.getName()));
            label.setText(timedAction.getSeconds() + " seconds");
        } else if (pointedAction.isRepsAction()) {
            final RepsAction repsAction = (RepsAction) pointedAction.action;
            final Exercise exercise = repsAction.getExercise();
            setImage(imageView, exercise.getImage());
            iconView.setImageResource(R.drawable.ic_reps);
            title.setText(capitalize(exercise.getName()));
            label.setText(repsAction.getReps() + " reps");
        } else if (pointedAction.isTimedRecoveryAction()) {
            final TimedRecovery timedRecovery = (TimedRecovery) pointedAction.action;
            iconView.setImageResource(R.drawable.ic_watch);
            title.setText("Timed recovery");
            label.setText(timedRecovery.getSeconds() + " seconds");
        } else if (pointedAction.isRecoveryAction()) {
            final Recovery recovery = (Recovery) pointedAction.action;
            iconView.setImageResource(R.drawable.ic_reps);
            title.setText("Recovery");
            label.setText("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGoButtonClick(View view) {
        state.navigator.navigateTo(CountdownActivity.class);
    }

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase().replaceAll("[_-]", " ");
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
