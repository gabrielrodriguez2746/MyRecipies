package com.myrecipes.utils;

import androidx.annotation.Nullable;

import java.util.List;

public final class ListUtils {

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

}