package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.ProgramFileRepository;
import pl.edu.agh.pockettrainer.program.repository.ProgramRepository;

public class ProgramDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {

            final String id = extras.getString("programId");

            // TODO this is redundant, should be able to pass Program object directly rather than I/O and JSON parsing
            final ProgramRepository programs = CachedProgramRepository.getInstance(this);
            DecoratedProgram program = programs.getById(id);

            setTitle(program.getMetadata().getName());

            final TextView labelProgram = findViewById(R.id.label_program);
            labelProgram.setText(program.getMetadata().getName());
        }
    }
}
