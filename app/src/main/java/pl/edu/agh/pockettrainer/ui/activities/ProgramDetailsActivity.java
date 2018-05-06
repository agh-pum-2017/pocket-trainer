package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import pl.edu.agh.pockettrainer.R;

public class ProgramDetailsActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //About
            ProgramDetailsListFragment fragmentDetailsList = new ProgramDetailsListFragment();
            fragmentDetailsList.setArguments(extras);
            fragmentTransaction.add(R.id.fragment_program_details_list,fragmentDetailsList);

            //Exercises
            ExerciseListFragment fragmentExerciseList = new ExerciseListFragment();
            fragmentExerciseList.setArguments(extras);
            fragmentTransaction.add(R.id.fragment_exercise_list,fragmentExerciseList);

            //Schedule
            DayListFragment fragmentDayList = new DayListFragment();
            fragmentDayList.setArguments(extras);
            fragmentTransaction.add(R.id.fragment_day_list,fragmentDayList);

            fragmentTransaction.commit();

            TabHost tabHost = findViewById(R.id.tabhost);
            tabHost.setup();

            //About
            TabHost.TabSpec specAbout = tabHost.newTabSpec("About");
            specAbout.setContent(R.id.About);
            specAbout.setIndicator("About");
            //Exercises
            TabHost.TabSpec specEcercises = tabHost.newTabSpec("Exercises");
            specEcercises.setContent(R.id.Exercises);
            specEcercises.setIndicator("Exercises");
            //Schedule
            TabHost.TabSpec specSchedule = tabHost.newTabSpec("Schedule");
            specSchedule.setContent(R.id.Schedule);
            specSchedule.setIndicator("Schedule");

            tabHost.addTab(specAbout);
            tabHost.addTab(specEcercises);
            tabHost.addTab(specSchedule);
        }
    }
}