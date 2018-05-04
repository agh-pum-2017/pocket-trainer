package pl.edu.agh.pockettrainer.program.repository.program;

import android.content.Context;

import pl.edu.agh.pockettrainer.program.repository.Repository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;

public class ProgramRepositoryFactory {

    private static CachedProgramRepository cachedFileInstance;

    public synchronized static ProgramRepository getCachedFileRepository(Context context, MetaRepository metaRepository) {
        if (cachedFileInstance == null) {
            cachedFileInstance = new CachedProgramRepository(new FileProgramRepository(context, metaRepository));
        }
        return cachedFileInstance;
    }
}
