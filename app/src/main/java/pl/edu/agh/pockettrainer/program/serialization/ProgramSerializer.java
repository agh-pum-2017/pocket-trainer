package pl.edu.agh.pockettrainer.program.serialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.program.domain.Exercise;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;
import pl.edu.agh.pockettrainer.program.domain.actions.Action;
import pl.edu.agh.pockettrainer.program.domain.actions.Recovery;
import pl.edu.agh.pockettrainer.program.domain.actions.RepsAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedAction;
import pl.edu.agh.pockettrainer.program.domain.actions.TimedRecovery;
import pl.edu.agh.pockettrainer.program.domain.days.Day;
import pl.edu.agh.pockettrainer.program.domain.days.RunningDay;
import pl.edu.agh.pockettrainer.program.domain.days.WorkoutDay;

public class ProgramSerializer {

    // Assumes LinkedHashMap<> in JSONObject for consistent MD5 hash sum calculation.

    private static final String SUPPORTED_FORMAT_VERSION = "1.0";

    public String serialize(TrainingProgram program) throws JSONException {
        final JSONObject json = new JSONObject();
        json.put("format_version", SUPPORTED_FORMAT_VERSION);
        json.put("program", serializeProgram(program));
        return json.toString();
    }

    private JSONObject serializeProgram(TrainingProgram program) throws JSONException {
        final JSONObject jsonProgram = new JSONObject();
        jsonProgram.put("metadata", serializeMetadata(program.getMetadata()));
        jsonProgram.put("definitions", serializeDefinitions(program));
        jsonProgram.put("schedule", serializeSchedule(program.getSchedule()));
        return jsonProgram;
    }

    private JSONObject serializeMetadata(Metadata metadata) throws JSONException {
        final JSONObject metadataJson = new JSONObject();
        metadataJson.put("author", metadata.getAuthor());
        metadataJson.put("name", metadata.getName());
        metadataJson.put("description", metadata.getDescription());
        metadataJson.put("targetGender", metadata.getTargetGender().name().toLowerCase());
        metadataJson.put("goals", serializeEnums(metadata.getGoals()));
        metadataJson.put("image", metadata.getImage().getAbsolutePath());
        return metadataJson;
    }

    private JSONObject serializeDefinitions(TrainingProgram program) throws JSONException {
        final JSONObject definitionsJson = new JSONObject();
        definitionsJson.put("exercises", serializeExercises(program));
        definitionsJson.put("days", serializeDays(program));
        return definitionsJson;
    }

    private JSONObject serializeExercises(TrainingProgram program) throws JSONException {
        final JSONObject exercisesJson = new JSONObject();
        for (Exercise exercise : sorted(program.getExercises())) {
            final JSONObject exerciseJson = new JSONObject();
            exerciseJson.put("muscles", serializeEnums(exercise.getMuscles()));
            exerciseJson.put("text", exercise.getDescription());
            exerciseJson.put("image", exercise.getImage().getAbsolutePath());
            exercisesJson.put(exercise.getName(), exerciseJson);
        }
        return exercisesJson;
    }

    private JSONObject serializeDays(TrainingProgram program) throws JSONException {
        final JSONObject daysJson = new JSONObject();
        final Set<Day> uniqueDays = new HashSet<>(program.getSchedule());
        for (Day day : sorted(uniqueDays)) {
            final JSONObject dayJson = new JSONObject();
            if (day.isRecovery()) {
                dayJson.put("type", "recovery");
            } else {
                if (day instanceof RunningDay) {
                    dayJson.put("type", "running");
                } else if (day instanceof WorkoutDay) {
                    dayJson.put("type", "workout");
                    dayJson.put("routine", serializeActions(((WorkoutDay) day).getRoutine()));
                }
            }
            daysJson.put(day.getName(), dayJson);
        }
        return daysJson;
    }

    private JSONArray serializeActions(List<Action> actions) throws JSONException {
        final JSONArray actionsJson = new JSONArray();
        for (Action action : actions) {
            final JSONObject actionJson = new JSONObject();
            if (action instanceof Recovery) {
                actionJson.put("type", "recovery");
            } else if (action instanceof TimedRecovery) {
                actionJson.put("type", "timed_recovery");
                actionJson.put("seconds", ((TimedRecovery) action).getSeconds());
            } else if (action instanceof TimedAction) {
                actionJson.put("type", ((TimedAction) action).getExercise().getName());
                actionJson.put("seconds", ((TimedAction) action).getSeconds());
            } else if (action instanceof RepsAction) {
                actionJson.put("type", ((RepsAction) action).getExercise());
                actionJson.put("seconds", ((RepsAction) action).getReps());
            }
            actionsJson.put(actionJson);
        }
        return actionsJson;
    }

    private JSONArray serializeSchedule(List<Day> schedule) {
        final JSONArray array = new JSONArray();
        for (Day day : schedule) {
            array.put("@" + day.getName());
        }
        return array;
    }

    private <T extends Enum<T>> JSONArray serializeEnums(Collection<T> enums) {
        final JSONArray array = new JSONArray();
        for (T value : sorted(enums)) {
            array.put(value.name().toLowerCase());
        }
        return array;
    }

    private <T extends Comparable<T>> List<T> sorted(Collection<T> collection) {
        final List<T> list = new ArrayList<>(collection);
        Collections.sort(list);
        return list;
    }
}
