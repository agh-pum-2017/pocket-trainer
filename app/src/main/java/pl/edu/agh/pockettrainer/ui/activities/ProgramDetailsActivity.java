package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.ProgramRepository;

public class ProgramDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {

            final ProgramRepository programs = CachedProgramRepository.getInstance(this);
            final DecoratedProgram program = programs.getById(extras.getString("programId"));

            setTitle(program.getMetadata().getName());

            // TODO
        }
    }
}
