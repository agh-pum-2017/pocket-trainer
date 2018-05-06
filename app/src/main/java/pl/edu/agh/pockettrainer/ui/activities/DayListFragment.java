package pl.edu.agh.pockettrainer.ui.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.DayAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayListFragment extends Fragment {
    List<Day> dayList;

    public DayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if(args!=null) {
            final ProgramRepository programRepository = ProgramRepositoryFactory.getCachedFileRepository(getContext());
            if(programRepository!=null) {
                String programId = args.getString("programId");
                if(programId!=null) {
                    final DecoratedProgram program = programRepository.getById(programId);
                    dayList = program.getSchedule();
                }
                else {
                    final DecoratedProgram program = programRepository.getById((String) args.get("programId"));
                    dayList = program.getSchedule();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_list,container,false);

        final Bundle args = getArguments();
        if(dayList!=null) {
            final ListView dayListView = view.findViewById(R.id.day_list);
            DayAdapter dayAdapter = new DayAdapter(getContext(), dayList);
            dayListView.setAdapter(dayAdapter);
        }
        return view;
    }
}
