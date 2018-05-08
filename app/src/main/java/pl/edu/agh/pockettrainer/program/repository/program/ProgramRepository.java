package pl.edu.agh.pockettrainer.program.repository.program;

import java.io.File;
import java.util.List;

import pl.edu.agh.pockettrainer.program.repository.Repository;

public interface ProgramRepository extends Repository {

    List<String> getBundledArchives();

    List<Program> getInstalled();

    void forceReload();

    Program getById(String id);

    void installResource(String path);

    void installLocalFile(File file);

    void installRemoteFile(String address);

    void uninstallAll();

    void uninstall(String programId);

    boolean hasActiveProgram();

    String getActiveProgramId();

    Program getActiveProgram();

    void setActiveProgram(Program program);

    void unsetActiveProgram();
}
