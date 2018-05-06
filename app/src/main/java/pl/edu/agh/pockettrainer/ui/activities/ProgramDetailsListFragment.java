package pl.edu.agh.pockettrainer.ui.activities;


import android.media.tv.TvContract;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.ProgramDetailsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramDetailsListFragment extends Fragment {
    List<DecoratedProgram> programList = new ArrayList<>();;

    public ProgramDetailsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if(args!=null){
            final ProgramRepository programRepository = ProgramRepositoryFactory.getCachedFileRepository(getContext());
            if(programRepository!=null) {
                String programId = args.getString("programId");
                final DecoratedProgram program;
                if(programId!=null) {
                    program = programRepository.getById(programId);
                }
                else {
                    program = programRepository.getById((String) args.get("programId"));
                }
                programList.add(program);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program_details_list, container, false);
        if(programList!=null){
            final ListView programDetailsListView = view.findViewById(R.id.program_details_list);
            ProgramDetailsAdapter programDetailsAdapter = new ProgramDetailsAdapter(getContext(),programList);
            programDetailsListView.setAdapter(programDetailsAdapter);
        }
        return view;
    }

}
