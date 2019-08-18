package com.myrecipes.data.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LazilyParsedNumber;
import com.myrecipes.data.models.Ingredient;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.RecipesWrapper;
import com.myrecipes.data.models.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.myrecipes.helpers.Helpers.getGenericOrDefault;

public class RecipesWrapperDeserializer implements JsonDeserializer<RecipesWrapper> {

    @Override
    public RecipesWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Timber.d("response object :: %s", json.toString());
        JsonArray recipes = json.getAsJsonArray();
        ArrayList<Recipe> mappedRecipes = new ArrayList<>();
        for (JsonElement recipe : recipes) {
            Recipe mappedRecipe = mapRecipeItemFromElement(recipe.getAsJsonObject());
            if (mappedRecipe != null) {
                mappedRecipes.add(mappedRecipe);
            }
        }
        return new RecipesWrapper(mappedRecipes);
    }

    private Recipe mapRecipeItemFromElement(JsonObject recipeObject) {
        Timber.d("recipe object :: %s", recipeObject.toString());
        int id = getGenericOrDefault(recipeObject, "id", new LazilyParsedNumber("-1")).intValue();
        if (id == -1) {
            return null;
        } else {
            String name = getGenericOrDefault(recipeObject, "name", "");
            List<Ingredient> ingredients = mapIngredientsFromElement(id,
                    getGenericOrDefault(recipeObject, "ingredients", new JsonArray())
            );
            List<Step> steps = mapStepsFromElement(id,
                    getGenericOrDefault(recipeObject, "steps", new JsonArray())
            );
            String nullableImage = getGenericOrDefault(recipeObject, "image", "");
            String image = !nullableImage.isEmpty() ? nullableImage : null;
            return new Recipe(id, name, image, ingredients, steps);
        }
    }

    private List<Ingredient> mapIngredientsFromElement(int recipeId, JsonArray ingredients) {
        Timber.d("ingredients object :: %s", ingredients.toString());
        ArrayList<Ingredient> mappedIngredients = new ArrayList<>();
        for (JsonElement ingredient : ingredients) {
            JsonObject ingredientObject = ingredient.getAsJsonObject();
            double quantity = getGenericOrDefault(
                    ingredientObject, "quantity", new LazilyParsedNumber("-1.0")
            ).doubleValue();
            if (quantity != -1.0) {
                String unit = getGenericOrDefault(ingredientObject, "measure", "");
                String name = getGenericOrDefault(ingredientObject, "ingredient", "");
                mappedIngredients.add(new Ingredient(recipeId, quantity, unit, name));
            }
        }
        return mappedIngredients;
    }

    private List<Step> mapStepsFromElement(int recipeId, JsonArray steps) {
        Timber.d("steps object :: %s", steps.toString());
        ArrayList<Step> mappedSteps = new ArrayList<>();
        for (JsonElement step : steps) {
            JsonObject stepObject = step.getAsJsonObject();
            int id = getGenericOrDefault(stepObject, "id", new LazilyParsedNumber("-1")).intValue();
            if (id != -1) {
                String shortDescription = getGenericOrDefault(stepObject, "shortDescription", "");
                String description = getGenericOrDefault(stepObject, "description", "");
                String videoURL = getGenericOrDefault(stepObject, "videoURL", "");
                String thumbnailVideo = getGenericOrDefault(stepObject, "thumbnailURL", "");
                String video = !videoURL.isEmpty() ? videoURL :
                        !thumbnailVideo.isEmpty() ? thumbnailVideo : null;
                mappedSteps.add(new Step(recipeId, id, shortDescription, description, video));
            }

        }
        return mappedSteps;
    }
}
