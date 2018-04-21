package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.TabHostSwipeAdapter;

public class ProgramDetailsActivity_temp extends AppCompatActivity {//
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details_temp);

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //Setting of program details into About
            final ProgramRepository programRepository = ProgramRepositoryFactory.getCachedFileRepository(this);
            final DecoratedProgram program = programRepository.getById(extras.getString("programId"));
            setTitle(program.getMetadata().getName());

            TabHostSwipeAdapter swipeAdapter = new TabHostSwipeAdapter(getSupportFragmentManager(),this);//, (ArrayAdapter<DecoratedProgram>) programs);
            viewPager.setAdapter(swipeAdapter);
            viewPager.setCurrentItem(extras.getInt("position",0));
            // TODO
        }
    }
}
