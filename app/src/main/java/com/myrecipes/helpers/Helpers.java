package com.myrecipes.helpers;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

import timber.log.Timber;

public class Helpers {

    public static <T> T getGenericOrDefault(JsonObject jsonObject, String key, T defaultValue) {
        return tryOrDefault(() -> {
            T internalValue = defaultValue;
            JsonElement element = jsonObject.get(key);
            if (element != null) {
                if (!element.isJsonNull()) {
                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitiveValue = element.getAsJsonPrimitive();
                        if (primitiveValue.isBoolean()) {
                            internalValue = convertInstanceOfObject(primitiveValue.getAsBoolean());
                        } else if (primitiveValue.isNumber()) {
                            internalValue = convertInstanceOfObject(primitiveValue.getAsNumber());
                        } else if (primitiveValue.isString()) {
                            internalValue = convertInstanceOfObject(primitiveValue.getAsString());
                        } else {
                            internalValue = convertInstanceOfObject(primitiveValue);
                        }
                    } else if (element.isJsonArray()) {
                        internalValue = convertInstanceOfObject(element.getAsJsonArray());
                    } else if (element.isJsonObject()) {
                        internalValue = convertInstanceOfObject(element.getAsJsonObject());
                    } else {
                        internalValue = convertInstanceOfObject(element);
                    }
                }
            }
            return internalValue;
        }, defaultValue);
    }

    public static String mapToListedString(List<String> stringList, @Nullable String itemsSeparator) {
        StringBuilder builder = new StringBuilder();
        int stringListSize = stringList.size();
        for (int index = 0; index < stringListSize; index++) {
            if (itemsSeparator != null && stringListSize != 1) {
                builder.append(itemsSeparator);
            }
            builder.append(stringList.get(index));
            if (index < stringListSize - 1) {
                builder.append(System.getProperty("line.separator"));
            }
        }
        return builder.toString();
    }

    private static <T> T tryOrDefault(GenericSupplier<T> function, T defaultValue) {
        T value = defaultValue;
        try {
            value = function.invoke();
        } catch (Exception e) {
            Timber.d(e.getLocalizedMessage());
        }
        return value;
    }


    @SuppressWarnings("unchecked")
    private static <T> T convertInstanceOfObject(Object object) {
        try {
            return (T) object;
        } catch (ClassCastException e) {
            return null;
        }
    }

}
