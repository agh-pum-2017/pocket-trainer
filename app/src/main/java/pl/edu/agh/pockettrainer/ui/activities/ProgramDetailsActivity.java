package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.FileProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.DayAdapter;
import pl.edu.agh.pockettrainer.ui.TabHostSwipeAdapter;

public class ProgramDetailsActivity extends FragmentActivity {//AppCompatActivity {//
    ViewPager viewPagerAbout;
    ViewPager viewPagerSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        viewPagerAbout = (ViewPager)findViewById(R.id.view_pager_about);
        viewPagerSchedule = (ViewPager)findViewById(R.id.view_pager_schedule);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
            tabHost.setup();
            //About
            TabHost.TabSpec spec = tabHost.newTabSpec("About");
            spec.setContent(R.id.About);
            spec.setIndicator("About");
            tabHost.addTab(spec);
            //Exercises
            spec = tabHost.newTabSpec("Exercises");
            spec.setContent(R.id.Exercises);
            spec.setIndicator("Exercises");
            tabHost.addTab(spec);
            //Schedule
            spec = tabHost.newTabSpec("Schedule");
            spec.setContent(R.id.Schedule);
            spec.setIndicator("Schedule");
            tabHost.addTab(spec);

            //Setting of program details into About
            //final List<DecoratedProgram> programs = ProgramRepositoryFactory.getCachedFileRepository(this).getInstalled();
            ProgramRepository programRepository = ProgramRepositoryFactory.getCachedFileRepository(getApplicationContext());
            final DecoratedProgram program = programRepository.getById(extras.getString("programId"));
            //ProgramRepository programs = ProgramRepositoryFactory.getCachedFileRepository(getApplicationContext());
            ProgramDetailsSwipeAdapter swipeAdapter = new ProgramDetailsSwipeAdapter(getSupportFragmentManager(),this);//, (ArrayAdapter<DecoratedProgram>) programs);
            Log.d("SWP","[before] viewPagerAbout.setAdapter(swipeAdapter);");
            viewPagerAbout.setAdapter(swipeAdapter);
            viewPagerAbout.setCurrentItem(extras.getInt("position",0));

            //Setting of program schedule into Schedule
            DayListSwipeAdapter dayListSwipeAdapter = new DayListSwipeAdapter(getSupportFragmentManager(),this);
            viewPagerSchedule.setAdapter(dayListSwipeAdapter);
            viewPagerSchedule.setCurrentItem(extras.getInt("position",0));

            tabHost.setCurrentTabByTag("Schedule");
            //setTitle(program.getMetadata().getName());
            // TODO
        }
    }
}/*
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //Setting of program details into About
            final ProgramRepository programRepository = ProgramRepositoryFactory.getCachedFileRepository(this);
            final DecoratedProgram program = programRepository.getById(extras.getString("programId"));
            setTitle(program.getMetadata().getName());

            TabHostSwipeAdapter swipeAdapter = new TabHostSwipeAdapter(getSupportFragmentManager(),this);//, (ArrayAdapter<DecoratedProgram>) programs);
            Log.d("SWP","ProgramDetailsActivity: swipeAdapter.getCount() = "+Integer.toString(swipeAdapter.getCount()));
            if(viewPager==null) Log.d("SWP","ERROR!  viewPager==null");
            viewPager.setAdapter(swipeAdapter);
            viewPager.setCurrentItem(extras.getInt("position",0));
            // TODO
        }
    }
}//*/

        /*

//*/