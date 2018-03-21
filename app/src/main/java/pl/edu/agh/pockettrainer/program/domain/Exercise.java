package pl.edu.agh.pockettrainer.program.domain;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Set;

public class Exercise implements Comparable<Exercise> {

    private final String name;
    private final Set<Muscle> muscles;
    private final String description;
    private final File image;

    public Exercise(String name,
                    Set<Muscle> muscles,
                    String description,
                    File image) {
        this.name = name;
        this.description = description;
        this.muscles = muscles;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Set<Muscle> getMuscles() {
        return muscles;
    }

    public String getDescription() {
        return description;
    }

    public File getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", muscles=" + muscles +
                ", description='" + description + '\'' +
                ", image=" + image +
                '}';
    }

    @Override
    public int compareTo(@NonNull Exercise other) {
        return name.compareTo(other.name);
    }
}
