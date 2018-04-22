package pl.edu.agh.pockettrainer.ui.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Gender;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.FileProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.ProgramAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramDetailsFragment extends Fragment {
    Metadata metadata;

    public ProgramDetailsFragment() {
        // Required empty public constructor
    }
    public void SetMetadata(Metadata metadata){
        this.metadata = metadata;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        final ProgramRepository programRepository = ProgramRepositoryFactory.getCachedFileRepository(getContext());
        final DecoratedProgram program = programRepository.getById(args.getString("programId"));
        metadata = program.getMetadata();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_details,container,false);

        final ImageView imageView = view.findViewById(R.id.program_image);
        final ImageView genderView = view.findViewById(R.id.gender_image);
        final TextView title = view.findViewById(R.id.label_title);
        final TextView goals = view.findViewById(R.id.label_goals);
        final TextView description = view.findViewById(R.id.label_description);
        final TextView author = view.findViewById(R.id.label_author);
        //savedInstanceState.getString("programId");

        if(container != null){
            Log.d("SWP","ProgramDetailsFragment: container.getTransitionName() = " + container.getTransitionName());
        }else{
            Log.d("SWP","ProgramDetailsFragment: container = null");
        }

        Glide.with(view).load(metadata.getImage()).into(imageView);//set Image
        title.setText(metadata.getName());
        goals.setText(makeString(metadata.getGoals()));
        description.setText(metadata.getDescription());
        author.setText(metadata.getAuthor());
        switch (metadata.getTargetGender()){
            case ANY:
                Glide.with(view).load(R.drawable.gender_any).into(genderView);
                break;
            case FEMALE:
                Glide.with(view).load(R.drawable.gender_female).into(genderView);
                break;
            case MALE:
                Glide.with(view).load(R.drawable.gender_male).into(genderView);
                break;
        }

        return view;
    }
    private String makeString(Set<ProgramGoal> goals) {
        final List<String> strings = new ArrayList<>();
        for (ProgramGoal goal : goals) {
            strings.add(goal.name().toLowerCase().replaceAll("_", " "));
        }
        Collections.sort(strings);
        String result = strings.toString();
        return result.substring(1, result.length() - 1);
    }
}
