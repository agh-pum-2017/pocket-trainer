package pl.edu.agh.pockettrainer.ui;

import android.content.Context;
import android.os.Bundle;
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
//    private List<TabHostFragment> tabHostFragments;
    public TabHostSwipeAdapter(FragmentManager fm, Context context) {
        super(fm);
        if(fm==null) Log.d("SWP","fm = null, there is the problem");
        this.programs = ProgramRepositoryFactory.getCachedFileRepository(context).getInstalled();
        Log.d("SWP","TabHostSwipeAdapter: getItem: TabHostSwipeAdapter created, programs size: "+Integer.toString(programs.size()));
//        for(int i=0;i<programs.size();i++){
//            tabHostFragments.add(new TabHostFragment());
//            tabHostFragments.get(i).SetPrograms(programs.get(i));
//        }
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("SWP","TabHostSwipeAdapter: getItem: public DayListFragment getItem(int position = " + Integer.toString(position) + ")");
        Log.d("SWP","TabHostSwipeAdapter: getItem: public DayListFragment programs.size() = " + Integer.toString(programs.size()));
        //DecoratedProgram program = programs.get(position);
//        Bundle args = new Bundle();
//        args.putInt("position",position);
        Log.d("SWP","TabHostSwipeAdapter: getItem: return tabHostFragments.get(position);");
        TabHostFragment fragment = new TabHostFragment();
        //Log.d("SWP","TabHostSwipeAdapter: getItem: fragment.SetPrograms(program);");
        fragment.SetPrograms(programs.get(position).getId());
        return fragment;
    }

    @Override
    public int getCount() {
        return programs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return programs.get(position).getMetadata().getName();
    }
}
