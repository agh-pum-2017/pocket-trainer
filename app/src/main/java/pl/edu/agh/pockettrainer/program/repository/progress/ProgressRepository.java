package pl.edu.agh.pockettrainer.program.repository.progress;

import pl.edu.agh.pockettrainer.program.repository.Repository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;

public interface ProgressRepository extends Repository {

    Progress getProgress(Program program);

    void update(Progress progress);
}
