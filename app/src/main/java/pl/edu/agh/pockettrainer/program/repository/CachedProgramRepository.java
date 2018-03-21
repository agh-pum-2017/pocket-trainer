package pl.edu.agh.pockettrainer.program.repository;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.pockettrainer.program.repository.io.Cache;

public class CachedProgramRepository implements ProgramRepository {

    private final ProgramRepository wrapped;
    private final Cache<String, DecoratedProgram> cache = new Cache<>();

    private static CachedProgramRepository instance;

    public synchronized static ProgramRepository getInstance(Context context) {

        if (instance == null) {
            instance = new CachedProgramRepository(new ProgramFileRepository(context));
        }

        return instance;
    }

    private CachedProgramRepository(ProgramRepository wrapped) {
        this.wrapped = wrapped;
    }

    public void invalidateCache() {
        cache.invalidate();
    }

    @Override
    public List<String> getBundledArchives() {
        return wrapped.getBundledArchives();
    }

    @Override
    public List<DecoratedProgram> getInstalled() {

        if (cache.isEmpty()) {
            for (DecoratedProgram program : wrapped.getInstalled()) {
                cache.set(program.getId(), program);
            }
        }

        return Sorter.sortedByName(new ArrayList<>(cache.values()));
    }

    @Override
    public DecoratedProgram getById(String id) {
        return cache.getOrSet(id, new Cache.ValueProvider<String, DecoratedProgram>() {
            @Override
            public DecoratedProgram get(String key) {
                return wrapped.getById(key);
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
    public DecoratedProgram getActiveProgram() {
        return wrapped.getActiveProgram();
    }

    @Override
    public void setActiveProgram(DecoratedProgram program) {
        wrapped.setActiveProgram(program);
    }

    @Override
    public void unsetActiveProgram() {
        wrapped.unsetActiveProgram();
    }
}
