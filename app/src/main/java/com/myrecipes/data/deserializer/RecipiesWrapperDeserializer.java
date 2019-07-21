package com.myrecipes.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.myrecipes.data.models.RecipesWrapper;

import java.lang.reflect.Type;

public class RecipiesWrapperDeserializer implements JsonDeserializer<RecipesWrapper> {

    @Override
    public RecipesWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new RecipesWrapper();
    }
}
