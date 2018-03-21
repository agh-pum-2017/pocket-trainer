package pl.edu.agh.pockettrainer.program.domain.actions;

public class TimedRecovery implements Action {

    private final int seconds;

    public TimedRecovery(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public boolean isRecovery() {
        return true;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return "TimedRecovery{" +
                "seconds=" + seconds +
                '}';
    }
}
