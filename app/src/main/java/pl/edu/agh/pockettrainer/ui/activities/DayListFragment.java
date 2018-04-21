package pl.edu.agh.pockettrainer.ui.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.ui.DayAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayListFragment extends Fragment {
    List<Day> dayList;
    //Context context;
    public DayListFragment() {
        // Required empty public constructor
    }
    public void SetDayList(List<Day> dayList){//,Context context){
        this.dayList = dayList;
        //this.context = context;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_list,container,false);

        final ListView dayListView = view.findViewById(R.id.day_list);
        Log.d("SWP","DayAdapter dayAdapter = new DayAdapter(getContext(),dayList);");
        DayAdapter dayAdapter = new DayAdapter(getContext(),dayList);
        Log.d("SWP","dayListView.setAdapter(dayAdapter);");
        dayListView.setAdapter(dayAdapter);
        return view;
    }
}
