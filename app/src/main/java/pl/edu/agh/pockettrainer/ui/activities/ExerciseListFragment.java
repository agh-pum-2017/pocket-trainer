package pl.edu.agh.pockettrainer.ui.activities;


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
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.ExerciseAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseListFragment extends Fragment {
    List<Exercise> exerciseList = new ArrayList<>();

    public ExerciseListFragment() {
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
                exerciseList.addAll(program.getExercises());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        if(exerciseList!=null) {
            final ListView exerciseListView = view.findViewById(R.id.exercise_list);
            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(getContext(),exerciseList);
            exerciseListView.setAdapter(exerciseAdapter);
        }
        return view;
    }

}
