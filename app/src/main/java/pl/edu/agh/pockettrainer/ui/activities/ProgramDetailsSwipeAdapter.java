package pl.edu.agh.pockettrainer.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.ProgramAdapter;

/**
 * Created by Mateusz on 4/11/2018.
 */

class ProgramDetailsSwipeAdapter extends FragmentStatePagerAdapter {
    private List<DecoratedProgram> programs;

    public ProgramDetailsSwipeAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.programs = ProgramRepositoryFactory.getCachedFileRepository(context).getInstalled();
    }

    @Override
    public ProgramDetailsFragment getItem(int position) {
        DecoratedProgram program = programs.get(position);
        final Metadata metadata = program.getMetadata();

        ProgramDetailsFragment fragment = new ProgramDetailsFragment();
        fragment.SetMetadata(metadata);

        return fragment;
    }

    @Override
    public int getCount() {
        return programs.size();
    }
}
