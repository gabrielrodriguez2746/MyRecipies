package com.myrecipes;

import androidx.test.runner.AndroidJUnitRunner;

import com.squareup.rx2.idler.Rx2Idler;

import io.reactivex.plugins.RxJavaPlugins;

public final class MyTestRunner extends AndroidJUnitRunner {
  @Override public void onStart() {
    RxJavaPlugins.setInitComputationSchedulerHandler(
        Rx2Idler.create("RxJava 2.x Computation Scheduler"));
    RxJavaPlugins.setInitIoSchedulerHandler(
            Rx2Idler.create("RxJava 2.x IO Scheduler"));
    super.onStart();
  }
}