package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;

public class ProgramDetailsActivity extends FragmentActivity {
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //final List<DecoratedProgram> programs = ProgramRepositoryFactory.getCachedFileRepository(this).getInstalled();
            //final DecoratedProgram program = programs.getById(extras.getString("programId"));
            //ProgramRepository programs = ProgramRepositoryFactory.getCachedFileRepository(getApplicationContext());
            ProgramDetailsSwipeAdapter swipeAdapter = new ProgramDetailsSwipeAdapter(getSupportFragmentManager(),this);//, (ArrayAdapter<DecoratedProgram>) programs);
            //setTitle(program.getMetadata().getName());
            viewPager.setAdapter(swipeAdapter);
            // TODO
        }
    }
}
