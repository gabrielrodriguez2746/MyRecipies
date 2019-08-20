package com.myrecipes.data.models;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "step")
public class Step {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_key")
    private int primaryKey;

    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    private int id;

    @ColumnInfo(name = "short_description")
    private String shortDescription;

    private String description;

    @Nullable
    private String video;

    public Step(int recipeId, int id, String shortDescription, String description, @Nullable String video) {
        this.recipeId = recipeId;
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.video = video;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
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
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return id == step.id &&
                Objects.equals(shortDescription, step.shortDescription) &&
                Objects.equals(description, step.description) &&
                Objects.equals(video, step.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortDescription, description, video);
    }

    @Override
    public String toString() {
        return "Step{" +
                "recipeId=" + recipeId +
                ", id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
