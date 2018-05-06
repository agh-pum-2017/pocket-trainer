package pl.edu.agh.pockettrainer.ui.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class ProgramDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00A6FF")));
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_white);
        }

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {

            final String id = extras.getString("programId");

            final ApplicationState state = (ApplicationState) getApplicationContext();
            final Program program = state.programRepository.getById(id);

            setTitle(Html.fromHtml("<font color='#ffffff'>" + program.getMetadata().getName() + "</font>"));

            // TODO
        }
    }
}
