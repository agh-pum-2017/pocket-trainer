package pl.edu.agh.pockettrainer.program.repository.program;

import java.io.File;
import java.util.List;

public interface ProgramRepository {

    List<String> getBundledArchives();

    List<DecoratedProgram> getInstalled();

    DecoratedProgram getById(String id);

    void installResource(String path);

    void installLocalFile(File file);

    void installRemoteFile(String address);

    void uninstallAll();

    void uninstall(String programId);

    boolean hasActiveProgram();

    String getActiveProgramId();

    DecoratedProgram getActiveProgram();

    void setActiveProgram(DecoratedProgram program);

    void unsetActiveProgram();
}
