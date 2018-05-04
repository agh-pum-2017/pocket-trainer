package pl.edu.agh.pockettrainer.program.serialization.program;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.Gender;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.Muscle;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.ActionGoal;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.domain.days.DayType;
import pl.edu.agh.pockettrainer.program.domain.days.RecoveryDay;
import pl.edu.agh.pockettrainer.program.domain.days.RunningDay;
import pl.edu.agh.pockettrainer.program.domain.days.WorkoutDay;
import pl.edu.agh.pockettrainer.program.repository.io.IoUtils;
import pl.edu.agh.pockettrainer.program.serialization.json.Converter;
import pl.edu.agh.pockettrainer.program.serialization.json.Json;

public class ProgramDeserializer {

    private static final String SUPPORTED_FORMAT_VERSION = "1.0";
        private static final Logger logger = new Logger(ProgramDeserializer.class);

    public static ProgramDeserializer withOriginalPaths() {
        return new ProgramDeserializer();
    }

    public static ProgramDeserializer withPathReplacement(final File sourceDir, final File outputDir) {
        return new ProgramDeserializer() {
            @Override
            protected File parsePath(String path) {
                try {
                    final File src = new File(sourceDir, path);
                    final File dst = new File(outputDir, IoUtils.md5(src));
                    try {
                        if (dst.exists()) {
                            logger.debug("Ignoring file which already exists: '%s'", dst.getAbsolutePath());
                        } else {
                            IoUtils.copy(src, dst);
                        }
                        return dst;
                    } catch (IOException ex) {
                        logger.error(ex, "Unable to replace '%s' with '%s'", src.getAbsolutePath(), dst.getAbsolutePath());
                    }
                } catch (IOException | NoSuchAlgorithmException ex) {
                    logger.error(ex, "Unable to calculate MD5 hash sum");
                }
                return super.parsePath(path);
            }
        };
    }

    private ProgramDeserializer() {
    }

    public TrainingProgram parse(String jsonString) throws JSONException {
        final Json json = new Json(jsonString);
        checkFormatVersion(json);
        return parse(json);
    }

    protected File parsePath(String path) {
        return new File(path);
    }

    private void checkFormatVersion(Json json) throws JSONException {
        final String formatVersion = json.get("format_version").asString();
        if (!SUPPORTED_FORMAT_VERSION.equals(formatVersion)) {
            throw new JSONException("Unsupported format version: " + formatVersion);
        }
    }

    private TrainingProgram parse(Json json) {

        final Map<String, Exercise> exerciseMap = parseExercises(json);
        final Map<String, Day> dayMap = parseDays(exerciseMap, json);

        return new TrainingProgram(
            parseMetadata(json),
            new HashSet<>(exerciseMap.values()),
            parseSchedule(dayMap, json));
    }

    private Metadata parseMetadata(Json json) {
        final Json parent = json.get("program", "metadata").asJson();
        return new Metadata(
            parent.get("author").asString(),
            parent.get("name").asString(),
            parent.get("description").asString(),
            parent.get("targetGender").asEnum(Gender.class),
            parent.get("goals").asEnums(ProgramGoal.class),
            parsePath(parent.get("image").asString())
        );
    }

    private Map<String, Exercise> parseExercises(Json json) {
        final Map<String, Object> children = json.get("program", "definitions", "exercises").children();
        final Map<String, Exercise> exerciseMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : children.entrySet()) {
            final Json child = new Json((JSONObject) entry.getValue());
            final Exercise exercise = new Exercise(
                entry.getKey(),
                child.get("muscles").asEnums(Muscle.class),
                child.get("text").asString(),
                parsePath(child.get("image").asString()));
            exerciseMap.put(entry.getKey(), exercise);
        }
        return exerciseMap;
    }

    private Map<String, Day> parseDays(Map<String, Exercise> exerciseMap, Json json) {
        final Map<String, Object> children = json.get("program", "definitions", "days").children();
        final Map<String, Day> dayMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : children.entrySet()) {
            final Json child = new Json((JSONObject) entry.getValue());
            final DayType dayType = child.get("type").asEnum(DayType.class);
            dayMap.put(entry.getKey(), parseDay(exerciseMap, child, dayType, entry.getKey()));
        }
        return dayMap;
    }

    private Day parseDay(Map<String, Exercise> exerciseMap, Json child, DayType dayType, String name) {
        switch (dayType) {
            case RECOVERY:
                return new RecoveryDay(name);
            case RUNNING:
                return new RunningDay(name);
            case WORKOUT:
                return parseWorkout(exerciseMap, child, name);
            default:
                return null;
        }
    }

    private Day parseWorkout(final Map<String, Exercise> exerciseMap, Json day, String name) {
        return new WorkoutDay(name, day.get("routine").asList(new Converter<Action>() {
            @Override
            public Action convert(Object item) {
                final Json action = new Json((JSONObject) item);
                final String actionType = action.get("type").asString();
                if (actionType.startsWith("@")) {
                    final Exercise exercise = exerciseMap.get(actionType.substring(1));
                    final int value = action.get("value").asInteger();
                    switch (action.get("goal").asEnum(ActionGoal.class)) {
                        case REPS:
                            return new RepsAction(exercise, value);
                        case SECONDS:
                            return new TimedAction(exercise, value);
                    }
                } else if ("timed_recovery".equals(actionType)) {
                    return new TimedRecovery(action.get("seconds").asInteger());
                } else if ("recovery".equals(actionType)) {
                    return new Recovery();
                }
                return null;
            }
        }));
    }

    private List<Day> parseSchedule(Map<String, Day> dayMap, Json json) {
        final List<Day> schedule = new ArrayList<>();
        for (String reference : json.get("program", "schedule").asStringList()) {
            schedule.add(dayMap.get(reference.substring(1)));
        }
        return schedule;
    }
}
