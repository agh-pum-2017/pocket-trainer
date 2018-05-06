package pl.edu.agh.pockettrainer.program.repository.program.iterator;

public class Pointer {

    public final int dayIndex;
    public final int actionIndex;

    public Pointer(int dayIndex, int actionIndex) {
        this.dayIndex = dayIndex;
        this.actionIndex = actionIndex;
    }

    @Override
    public String toString() {
        return "Pointer{" +
                "dayIndex=" + dayIndex +
                ", actionIndex=" + actionIndex +
                '}';
    }
}
