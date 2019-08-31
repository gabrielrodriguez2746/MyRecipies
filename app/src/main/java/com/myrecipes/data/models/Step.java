package com.myrecipes.data.models;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "step")
public class Step {

    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    private int id;

    @PrimaryKey
    private int customId;

    @ColumnInfo(name = "short_description")
    private String shortDescription;

    private String description;

    private int index;

    @Nullable
    private String video;

    public Step(int index, int recipeId, int id, String shortDescription, String description, @Nullable String video) {
        this.index = index;
        this.recipeId = recipeId;
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.video = video;
        this.customId = recipeId + id + shortDescription.hashCode();
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public int getIndex() {
        return index;
    }

    public int getCustomId() {
        return customId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public String getVideo() {
        return video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Step)) return false;
        Step step = (Step) o;
        return recipeId == step.recipeId &&
                id == step.id &&
                customId == step.customId &&
                index == step.index &&
                shortDescription.equals(step.shortDescription) &&
                description.equals(step.description) &&
                Objects.equals(video, step.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, id, customId, shortDescription, description, index, video);
    }
}
