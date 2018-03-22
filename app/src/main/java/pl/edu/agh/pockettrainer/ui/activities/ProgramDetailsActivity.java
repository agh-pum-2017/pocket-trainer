package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;

public class ProgramDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {

            final ProgramRepository programs = ProgramRepositoryFactory.getCachedFileRepository(this);
            final DecoratedProgram program = programs.getById(extras.getString("programId"));

            setTitle(program.getMetadata().getName());

            // TODO
        }
    }
}
