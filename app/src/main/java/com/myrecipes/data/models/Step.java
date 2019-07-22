package com.myrecipes.data.models;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Step {

    private int id;

    private String shortDescription;

    private String description;

    @Nullable
    private String video;

    public Step(int id, String shortDescription, String description, @Nullable String video) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.video = video;
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
}
