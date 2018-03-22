package pl.edu.agh.pockettrainer.program.repository.program;

import android.content.Context;

public class ProgramRepositoryFactory {

    private static CachedProgramRepository cachedFileInstance;

    public synchronized static CachedProgramRepository getCachedFileRepository(Context context) {
        if (cachedFileInstance == null) {
            cachedFileInstance = new CachedProgramRepository(new FileProgramRepository(context));
        }
        return cachedFileInstance;
    }
}
