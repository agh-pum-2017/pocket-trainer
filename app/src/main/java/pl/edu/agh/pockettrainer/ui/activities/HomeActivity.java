package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.ProgramFileRepository;
import pl.edu.agh.pockettrainer.program.repository.ProgramRepository;

public class HomeActivity extends AppCompatActivity {

    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewComponents();
        setupView();
    }

    private void findViewComponents() {
        label = findViewById(R.id.label);
    }

    private void setupView() {

        final ProgramRepository programs = new ProgramFileRepository(this);
        final DecoratedProgram activeProgram = programs.getActiveProgram();

        if (activeProgram == null) {
            label.setVisibility(View.VISIBLE);
        } else {
            label.setVisibility(View.INVISIBLE);
        }

        // TODO load progress
        // TODO calculate some other stuff
        // TODO display active program
    }
}
