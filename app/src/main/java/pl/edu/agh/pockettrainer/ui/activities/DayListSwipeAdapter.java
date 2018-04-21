package pl.edu.agh.pockettrainer.ui.activities;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;

/**
 * Created by Mateusz on 4/11/2018.
 */

class DayListSwipeAdapter extends FragmentStatePagerAdapter {
    private List<DecoratedProgram> programs;

    public DayListSwipeAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.programs = ProgramRepositoryFactory.getCachedFileRepository(context).getInstalled();
    }

    @Override
    public DayListFragment getItem(int position) {
        Log.d("SWP","public DayListFragment getItem(int position = " + Integer.toString(position) + ")");
        Log.d("SWP","public DayListFragment programs.size() = " + Integer.toString(programs.size()));
        DecoratedProgram program = programs.get(position);
        final List<Day> dayList = program.getSchedule();

        DayListFragment fragment = new DayListFragment();
        fragment.SetDayList(dayList);

        return fragment;
    }

    @Override
    public int getCount() {
        return programs.size();
    }
}
