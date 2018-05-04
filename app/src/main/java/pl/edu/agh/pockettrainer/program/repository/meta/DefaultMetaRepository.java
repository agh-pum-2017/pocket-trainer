package pl.edu.agh.pockettrainer.program.repository.meta;

import android.content.Context;

import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.program.repository.progress.ProgressRepository;

public class DefaultMetaRepository implements MetaRepository {

    private final ProgramRepository programRepository;
    private final ProgressRepository progressRepository;

    public DefaultMetaRepository(Context context) {
        this.programRepository = ProgramRepositoryFactory.getCachedFileRepository(context, this);
        this.progressRepository = null; // TODO new DatabaseProgressRepository(context);
    }

    @Override
    public ProgramRepository getProgramRepository() {
        return programRepository;
    }

    @Override
    public ProgressRepository getProgressRepository() {
        return progressRepository;
    }
}
