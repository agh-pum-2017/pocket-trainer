package pl.edu.agh.pockettrainer.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.TabHost;

import java.util.List;

import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.activities.TabHostFragment;

/**
 * Created by Mateusz on 4/20/2018.
 */

public class TabHostSwipeAdapter extends FragmentStatePagerAdapter{
    private final List<DecoratedProgram> programs;

    public TabHostSwipeAdapter(FragmentManager fm, Context context) {
        super(fm);
        if(fm==null) Log.d("SWP","fm = null, there is the problem");
        this.programs = ProgramRepositoryFactory.getCachedFileRepository(context).getInstalled();
        Log.d("SWP","TabHostSwipeAdapter: getItem: TabHostSwipeAdapter created, programs size: "+Integer.toString(programs.size()));
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("SWP","TabHostSwipeAdapter: getItem: public DayListFragment getItem(int position = " + Integer.toString(position) + ")");
        Log.d("SWP","TabHostSwipeAdapter: getItem: public DayListFragment programs.size() = " + Integer.toString(programs.size()));
        DecoratedProgram program = programs.get(position);

        Log.d("SWP","TabHostSwipeAdapter: getItem: TabHostFragment fragment = new TabHostFragment();");
        TabHostFragment fragment = new TabHostFragment();
        Log.d("SWP","TabHostSwipeAdapter: getItem: fragment.SetPrograms(program);");
        fragment.SetPrograms(program);
        return fragment;
    }

    @Override
    public int getCount() {
        return programs.size();
    }
}
