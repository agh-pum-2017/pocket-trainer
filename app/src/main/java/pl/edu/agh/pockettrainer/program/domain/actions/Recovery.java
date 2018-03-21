package pl.edu.agh.pockettrainer.program.domain.actions;

public class Recovery implements Action {

    @Override
    public boolean isRecovery() {
        return true;
    }

    @Override
    public String toString() {
        return "Recovery{}";
    }
}
