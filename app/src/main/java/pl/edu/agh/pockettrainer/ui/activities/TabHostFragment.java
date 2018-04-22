package pl.edu.agh.pockettrainer.ui.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabHostFragment extends Fragment {
    private DecoratedProgram program;
    FragmentTabHost mTabHost;
    Bundle args;
    public TabHostFragment() {
        // Required empty public constructor
    }

    public void SetPrograms(String programId) {//DecoratedProgram program){
        //this.program = program;
        final ProgramRepository programRepository = ProgramRepositoryFactory.getCachedFileRepository(getContext());
        this.program = programRepository.getById(programId);
        Log.d("SWP","TabHostFragment: SetPrograms: program name = " + program.getMetadata().getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final Bundle args = getArguments();
//        Log.d("SWP","TabHostFragment: onCreate: program name = " + program.getMetadata().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("SWP","TabHostFragment: onCreateView: inflater.toString()="+inflater.toString());
        Log.d("SWP","TabHostFragment: onCreateView: container.getTransitionName()="+container.getTransitionName());
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tab_host, container,false);
        if(container != null){
            Log.d("SWP","TabHostFragment: container.getTransitionName() = " + container.getTransitionName());
        }else{
            Log.d("SWP","TabHostFragment: container = null");
        }
//        final TabHost tabHost = (TabHost) view.findViewById(R.id.tab_host);
//
//        final FragmentTransaction transaction=null;
//
//        tabHost.setup();
//        //About
//        TabHost.TabSpec spec = tabHost.newTabSpec("About");
//        spec.setContent(R.id.About);
//        spec.setIndicator("About");
//        tabHost.addTab(spec);
//        //Exercises
//        spec = tabHost.newTabSpec("Exercises");
//        spec.setContent(R.id.Exercises);
//        spec.setIndicator("Exercises");
//        tabHost.addTab(spec);
//        //Schedule
//        spec = tabHost.newTabSpec("Schedule");
//        spec.setContent(R.id.Schedule);
//        spec.setIndicator("Schedule");
//        tabHost.addTab(spec);
//
//        ProgramDetailsFragment programDetailsFragment = new ProgramDetailsFragment();
//        programDetailsFragment.SetMetadata(program.getMetadata());
//        transaction.add(R.id.fragment_about,programDetailsFragment);
//
//        DayListFragment dayListFragment = new DayListFragment();
//        dayListFragment.SetDayList(program.getSchedule());
//        transaction.add(R.id.fragment_schedule,dayListFragment);

//        FragmentTabHost mTabHost = (FragmentTabHost)view.findViewById(R.id.tab_host);
//        mTabHost.setup(getContext(), getActivity().getSupportFragmentManager(), R.id.realtabcontent);
//
//        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab1"),
//                ProgramDetailsFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Tab2"),
//                DayListFragment.class, null);

        mTabHost = view.findViewById(R.id.tab_host);
        mTabHost.setup(getContext(), getActivity().getSupportFragmentManager(), R.id.realtabcontent);
        args = new Bundle();
        args.putString("programId",program.getId());
//        ProgramDetailsFragment programDetailsFragment = new ProgramDetailsFragment();
//        programDetailsFragment.SetMetadata(program.getMetadata());


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mTabHost.addTab(mTabHost.newTabSpec("tababout").setIndicator("About"), ProgramDetailsFragment.class,args);
        mTabHost.addTab(mTabHost.newTabSpec("tabschedule").setIndicator("Schedule"), DayListFragment.class, args);
    }
}
