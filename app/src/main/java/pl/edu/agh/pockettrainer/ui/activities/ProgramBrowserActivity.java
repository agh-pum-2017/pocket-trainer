package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.ProgramRepository;
import pl.edu.agh.pockettrainer.ui.ProgramAdapter;

public class ProgramBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_browser);
        setTitle("Programs");

        final ProgramRepository programs = CachedProgramRepository.getInstance(this);
        final ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new ProgramAdapter(this, programs));
    }
}
