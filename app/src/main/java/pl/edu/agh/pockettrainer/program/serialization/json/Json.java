package pl.edu.agh.pockettrainer.program.serialization.json;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Json {

    private final JSONObject root;

    public Json(String jsonString) throws JSONException {
        this.root = new JSONObject(jsonString);
    }

    public Json(JSONObject jsonObject) {
        this.root = jsonObject;
    }

    public JsonExtractor get(String... path) {
        return new JsonExtractor(path);
    }

    public class JsonExtractor {

        private final String[] path;

        public JsonExtractor(String... path) {
            this.path = path;
        }

        public Map<String, Object> children() {

            Map<String, Object> children = new HashMap<>();

            JSONObject parent = getObject().asJSONObject();
            if (parent != null) {
                Iterator<String> keys = parent.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    children.put(key, parent.opt(key));
                }
            }

            return children;
        }

        public String asString() {
            return asStringOrDefault(null);
        }

        public String asStringOrDefault(String defaultValue) {
            return getObject().asString(defaultValue);
        }

        public Integer asInteger() {
            return asIntegerOrDefault(null);
        }

        public Integer asIntegerOrDefault(Integer defaultValue) {
            return getObject().asInteger(defaultValue);
        }

        public Long asLong() {
            return asLongOrDefault(null);
        }

        public Long asLongOrDefault(Long defaultValue) {
            return getObject().asLong(defaultValue);
        }

        public Double asDouble() {
            return asDoubleOrDefault(null);
        }

        public Double asDoubleOrDefault(Double defaultValue) {
            return getObject().asDouble(defaultValue);
        }

        public Boolean asBoolean() {
            return asBooleanOrDefault(null);
        }

        public Boolean asBooleanOrDefault(Boolean defaultValue) {
            return getObject().asBoolean(defaultValue);
        }

        public <T extends Enum<T>> T asEnum(Class<T> enumClass) {
            return asEnumOrDefault(enumClass, null);
        }

        @SuppressWarnings("unchecked")
        public <T extends Enum<T>> T asEnumOrDefault(@NonNull T defaultValue) {
            if (defaultValue == null) {
                return null;
            }
            Class<T> enumClass = (Class<T>) defaultValue.getClass();
            return asEnumOrDefault(enumClass, defaultValue);
        }

        public Json asJson() {
            return new Json(getObject().asJSONObject());
        }

        public List<Json> asJsonList() {
            return asList(new Converter<Json>() {
                @Override
                public Json convert(Object item) {
                    return new Json((JSONObject) item);
                }
            });
        }

        public List<String> asStringList() {
            return asList(new Converter<String>() {
                @Override
                public String convert(Object item) {
                    return item.toString();
                }
            });
        }

        public List<Integer> asIntegerList() {
            return asList(new Converter<Integer>() {
                @Override
                public Integer convert(Object item) {
                    try {
                        return Integer.parseInt(item.toString());
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                }
            });
        }

        public List<Long> asLongList() {
            return asList(new Converter<Long>() {
                @Override
                public Long convert(Object item) {
                    try {
                        return Long.parseLong(item.toString());
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                }
            });
        }

        public List<Double> asDoubleList() {
            return asList(new Converter<Double>() {
                @Override
                public Double convert(Object item) {
                    try {
                        return Double.parseDouble(item.toString());
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                }
            });
        }

        public List<Boolean> asBooleanList() {
            return asList(new Converter<Boolean>() {
                @Override
                public Boolean convert(Object item) {
                    return Boolean.valueOf(item.toString().trim());
                }
            });
        }

        public <T extends Enum<T>> List<T> asEnumList(final Class<T> enumClass) {
            return asList(new Converter<T>() {
                @Override
                public T convert(Object item) {
                    return asEnum(enumClass, item.toString(), null);
                }
            });
        }

        public <T extends Enum<T>> Set<T> asEnums(final Class<T> enumClass) {
            Set<T> enums = new HashSet<>(asEnumList(enumClass));
            enums.remove(null);
            return enums;
        }

        public <T> List<T> asList(Converter<T> converter) {
            final List<T> items = new ArrayList<>();
            try {
                final JSONArray array = getArray();
                for (int i = 0; i < array.length(); i++) {
                    items.add(converter.convert(array.get(i)));
                }
            } catch (JSONException ignored) {}
            return items;
        }

        private ParentObject getObject() {
            try {
                JSONObject parent = root;
                for (int i = 0; i < path.length - 1; i++) {
                    parent = parent.getJSONObject(path[i]);
                }
                return new ParentObject(parent, path[path.length - 1]);
            } catch (JSONException ex) {
                return new NullParentObject();
            }
        }

        private JSONArray getArray() throws JSONException {
            JSONObject parent = root;
            for (int i = 0; i < path.length - 1; i++) {
                parent = parent.getJSONObject(path[i]);
            }
            return parent.getJSONArray(path[path.length - 1]);
        }

        private <T extends Enum<T>> T asEnumOrDefault(Class<T> enumClass, T defaultValue) {
            String value = asString();
            return value == null ? defaultValue : asEnum(enumClass, value, defaultValue);
        }

        private <T extends Enum<T>> T asEnum(Class<T> enumClass, String name, T defaultValue) {
            try {
                return Enum.valueOf(enumClass, name.trim().toUpperCase());
            } catch (NullPointerException | IllegalArgumentException ex) {
                return defaultValue;
            }
        }
    }

    private static class ParentObject {

        private final JSONObject parent;
        private final String childName;

        ParentObject(JSONObject parent, String childName) {
            this.parent = parent;
            this.childName = childName;
        }

        JSONObject asJSONObject() {
            return parent.optJSONObject(childName);
        }

        String asString(String defaultValue) {
            return parent.optString(childName, defaultValue);
        }

        Integer asInteger(Integer defaultValue) {
            try {
                return parent.getInt(childName);
            } catch (JSONException ex) {
                return defaultValue;
            }
        }

        Long asLong(Long defaultValue) {
            try {
                return parent.getLong(childName);
            } catch (JSONException ex) {
                return defaultValue;
            }
        }

        Double asDouble(Double defaultValue) {
            try {
                return parent.getDouble(childName);
            } catch (JSONException ex) {
                return defaultValue;
            }
        }

        Boolean asBoolean(Boolean defaultValue) {
            try {
                return parent.getBoolean(childName);
            } catch (JSONException ex) {
                return defaultValue;
            }
        }
    }

    private static class NullParentObject extends ParentObject {

        NullParentObject() {
            super(null, null);
        }

        @Override
        String asString(String defaultValue) {
            return defaultValue;
        }

        @Override
        Integer asInteger(Integer defaultValue) {
            return defaultValue;
        }

        @Override
        Long asLong(Long defaultValue) {
            return defaultValue;
        }

        @Override
        Double asDouble(Double defaultValue) {
            return defaultValue;
        }

        @Override
        Boolean asBoolean(Boolean defaultValue) {
            return defaultValue;
        }
    }
}
