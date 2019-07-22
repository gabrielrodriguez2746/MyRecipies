package com.myrecipes.helpers;

interface GenericSupplier<T> {
    T invoke();
}