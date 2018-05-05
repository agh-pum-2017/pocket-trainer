package pl.edu.agh.pockettrainer.ui.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashSet;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.Muscle;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.Navigator;

public class UpcomingActivity extends AppCompatActivity {

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

        final MetaRepository metaRepository = new DefaultMetaRepository(this);
        final ProgramRepository programRepository = metaRepository.getProgramRepository();
        final Program program = programRepository.getActiveProgram();
        final Progress progress = program.getProgress();

        final Action action = progress.getNextAction();

        final ImageView imageView = findViewById(R.id.upcoming_action_image);
        final ImageView iconView = findViewById(R.id.upcoming_action_icon);
        final TextView title = findViewById(R.id.upcoming_action_title);
        final TextView label = findViewById(R.id.upcoming_action_label);

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
        final Navigator navigator = new Navigator(this);
        // TODO show exercise details (fragment of program details?)
    }

    public void onGoButtonClick(View view) {
        // TODO navigate to countdown, and then to player
        // TODO somehow serialize action with exercise to avoid loading it again
    }

    private void setImage(ImageView imageView, File imageFile) {
        Glide.with(this).load(imageFile).into(imageView);
    }

    private String capitalize(String string) {
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
