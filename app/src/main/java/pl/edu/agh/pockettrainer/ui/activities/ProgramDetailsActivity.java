package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class ProgramDetailsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {

            final String id = extras.getString("programId");

            final ApplicationState state = (ApplicationState) getApplicationContext();
            program = state.programRepository.getById(id);

            toolbar.setTitle(program.getMetadata().getName());
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new AboutFragment();
                    break;
                case 1:
                    fragment = new ExercisesFragment();
                    break;
                case 2:
                    fragment = new ScheduleFragment();
                    break;
            }

            if (fragment != null) {
                final Bundle args = new Bundle();
                args.putString("programId", program.getId());
                fragment.setArguments(args);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public static class AboutFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            final Bundle arguments = getArguments();
            final ApplicationState state = (ApplicationState) getContext().getApplicationContext();
            final Program program = state.programRepository.getById(arguments.getString("programId"));
            final Metadata metadata = program.getMetadata();

            final View rootView = inflater.inflate(R.layout.fragment_program_details_about, container, false);
            final TextView author = rootView.findViewById(R.id.program_details_about_author);
            final TextView description = rootView.findViewById(R.id.program_details_about_description);
            final TextView goals = rootView.findViewById(R.id.program_details_about_goals);
            final ImageView icon1 = rootView.findViewById(R.id.program_details_about_icon1);
            final ImageView icon2 = rootView.findViewById(R.id.program_details_about_icon2);

            author.setText(metadata.getAuthor());
            description.setText(metadata.getDescription());
            goals.setText(makeString(metadata.getGoals()));

            switch (metadata.getTargetGender()) {
                case MALE:
                    icon1.setVisibility(View.INVISIBLE);
                    icon2.setVisibility(View.VISIBLE);
                    icon2.setImageResource(R.drawable.male_icon);
                    break;
                case FEMALE:
                    icon1.setVisibility(View.INVISIBLE);
                    icon2.setVisibility(View.VISIBLE);
                    icon2.setImageResource(R.drawable.female_icon);
                    break;
                case ANY:
                    icon1.setVisibility(View.VISIBLE);
                    icon2.setVisibility(View.VISIBLE);
                    icon1.setImageResource(R.drawable.male_icon);
                    icon2.setImageResource(R.drawable.female_icon);
                    break;
                default:
                    icon1.setVisibility(View.INVISIBLE);
                    icon2.setVisibility(View.INVISIBLE);
            }

            return rootView;
        }

        private String makeString(Set<ProgramGoal> goals) {

            final List<String> strings = new ArrayList<>();
            for (ProgramGoal goal : goals) {
                strings.add(goal.name().toLowerCase().replaceAll("_", " "));
            }

            Collections.sort(strings);

            final StringBuilder sb = new StringBuilder();
            for (String goal : strings) {
                sb.append("• ").append(goal.toLowerCase()).append("\n");
            }

            return sb.toString();
        }
    }

    public static class ExercisesFragment extends Fragment {

    }

    public static class ScheduleFragment extends Fragment {

    }
}
