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
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
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
        final Action action = progress.getNextAction();

        state.action = action;

        if (action.isRecovery()) {
            state.futureAction = progress.getFutureAction();
        }

        final ImageView imageView = findViewById(R.id.upcoming_action_image);
        final ImageView iconView = findViewById(R.id.upcoming_action_icon);
        final TextView title = findViewById(R.id.upcoming_action_title);
        final TextView label = findViewById(R.id.upcoming_action_label);
        final TextView button = findViewById(R.id.upcoming_action_button_show);

        button.setVisibility(View.VISIBLE);

        if (action instanceof TimedAction) {
            final TimedAction timedAction = (TimedAction) action;
            final Exercise exercise = timedAction.getExercise();
            setImage(imageView, exercise.getImage());
            iconView.setImageResource(R.drawable.ic_watch);
            title.setText(capitalize(exercise.getName()));
            label.setText(timedAction.getSeconds() + " seconds");
        } else if (action instanceof RepsAction) {
            final RepsAction repsAction = (RepsAction) action;
            final Exercise exercise = repsAction.getExercise();
            setImage(imageView, exercise.getImage());
            iconView.setImageResource(R.drawable.ic_reps);
            title.setText(capitalize(exercise.getName()));
            label.setText(repsAction.getReps() + " reps");
        } else if (action instanceof TimedRecovery) {
            final TimedRecovery timedRecovery = (TimedRecovery) action;
            iconView.setImageResource(R.drawable.ic_watch);
            title.setText("Timed recovery");
            label.setText(timedRecovery.getSeconds() + " seconds");
            button.setVisibility(View.INVISIBLE);
        } else if (action instanceof Recovery) {
            final Recovery recovery = (Recovery) action;
            iconView.setImageResource(R.drawable.ic_reps);
            title.setText("Recovery");
            label.setText("");
            button.setVisibility(View.INVISIBLE);
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

    public void onShowButtonClick(View view) {
        // TODO show exercise details (fragment of program details?)
        // state.navigator.navigateTo();
    }

    public void onGoButtonClick(View view) {
        state.navigator.navigateTo(CountdownActivity.class);
    }

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
