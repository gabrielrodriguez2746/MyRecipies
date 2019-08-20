package com.myrecipes.widget;

import com.myrecipes.data.models.Step;

import java.util.List;

public class StepWidget {

    private List<Step> steps;

    private OnStepClicked listener;

    public StepWidget(List<Step> steps, OnStepClicked listener) {
        this.steps = steps;
        this.listener = listener;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public OnStepClicked getListener() {
        return listener;
    }
}
