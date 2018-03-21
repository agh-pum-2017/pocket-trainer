package pl.edu.agh.pockettrainer.program.domain;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class Metadata {

    private final String author;
    private final String name;
    private final String description;
    private final Gender targetGender;
    private final Set<ProgramGoal> goals;
    private final File image;

    public Metadata(String author,
                    String name,
                    String description,
                    Gender targetGender,
                    Set<ProgramGoal> goals,
                    File image) {
        this.author = author;
        this.name = name;
        this.description = description;
        this.targetGender = targetGender;
        this.goals = goals;
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Gender getTargetGender() {
        return targetGender;
    }

    public Set<ProgramGoal> getGoals() {
        return Collections.unmodifiableSet(goals);
    }

    public File getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", targetGender=" + targetGender +
                ", goals=" + goals +
                ", image=" + image +
                '}';
    }
}
