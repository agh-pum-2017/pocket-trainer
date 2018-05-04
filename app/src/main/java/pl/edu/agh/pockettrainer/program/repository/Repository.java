package pl.edu.agh.pockettrainer.program.repository;

import android.content.Context;

import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;

public interface Repository {

    MetaRepository getMetaRepository();

    Context getContext();
}
