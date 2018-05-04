package pl.edu.agh.pockettrainer.program.repository.program;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.pockettrainer.program.repository.io.Cache;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.progress.ProgressRepository;

public class CachedProgramRepository implements ProgramRepository {

    private final MetaRepository metaRepository;
    private final ProgramRepository wrapped;
    private final Cache<String, Program> cache = new Cache<>();

    public CachedProgramRepository(final ProgramRepository wrapped) {
        final ProgramRepository self = this;
        this.wrapped = wrapped;
        this.metaRepository = new MetaRepository() {
            @Override
            public ProgramRepository getProgramRepository() {
                return self;
            }

            @Override
            public ProgressRepository getProgressRepository() {
                return wrapped.getMetaRepository().getProgressRepository();
            }
        };
    }

    public void invalidateCache() {
        cache.invalidate();
    }

    @Override
    public MetaRepository getMetaRepository() {
        return metaRepository;
    }

    @Override
    public Context getContext() {
        return wrapped.getContext();
    }

    @Override
    public List<String> getBundledArchives() {
        return wrapped.getBundledArchives();
    }

    @Override
    public List<Program> getInstalled() {

        if (cache.isEmpty()) {
            for (Program program : wrapped.getInstalled()) {
                cache.set(program.getId(), injectCachedRepository(program));
            }
        }

        return Sorter.sortedByName(new ArrayList<>(cache.values()));
    }

    @Override
    public Program getById(String id) {
        return cache.getOrSet(id, new Cache.ValueProvider<String, Program>() {
            @Override
            public Program get(String key) {
                return injectCachedRepository(wrapped.getById(key));
            }
        });
    }

    @Override
    public void installResource(String path) {
        wrapped.installResource(path);
    }

    @Override
    public void installLocalFile(File file) {
        wrapped.installLocalFile(file);
    }

    @Override
    public void installRemoteFile(String address) {
        wrapped.installRemoteFile(address);
    }

    @Override
    public void uninstallAll() {
        wrapped.uninstallAll();
    }

    @Override
    public void uninstall(String programId) {
        wrapped.uninstall(programId);
    }

    @Override
    public boolean hasActiveProgram() {
        return wrapped.hasActiveProgram();
    }

    @Override
    public String getActiveProgramId() {
        return wrapped.getActiveProgramId();
    }

    @Override
    public Program getActiveProgram() {
        return getById(getActiveProgramId());
    }

    @Override
    public void setActiveProgram(Program program) {
        wrapped.setActiveProgram(program);
    }

    @Override
    public void unsetActiveProgram() {
        wrapped.unsetActiveProgram();
    }

    private Program injectCachedRepository(Program program) {
        return program == null ? null : new Program(metaRepository, program.getId(), program.get());
    }
}
