package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;

public class ProgramDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {

            final String id = extras.getString("programId");

            final MetaRepository metaRepository = new DefaultMetaRepository(this);
            final ProgramRepository programRepository = metaRepository.getProgramRepository();
            final Program program = programRepository.getById(id);

            setTitle(program.getMetadata().getName());

            // TODO
        }
    }
}
