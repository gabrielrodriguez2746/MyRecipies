package com.myrecipes.base.providers;

import androidx.annotation.StringRes;

public interface ResourceProvider {

    String getString(@StringRes int id);
}
