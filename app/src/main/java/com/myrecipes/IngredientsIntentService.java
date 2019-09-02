package com.myrecipes;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.myrecipes.data.models.Ingredient;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.respositories.IngredientsRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static android.app.NotificationManager.IMPORTANCE_MIN;
import static androidx.core.app.NotificationCompat.CATEGORY_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

public class IngredientsIntentService extends IntentService {

    private Disposable disposable;

    @Inject
    IngredientsRepository repository;


    public IngredientsIntentService() {
        super("IngredientsIntentService");
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground();
        } else {
            startForeground(1, new Notification());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("Receiving intent");
        disposable = repository.getLastRecipeIngredients()
                .doOnSuccess(this::updateWidgets)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recipe -> {
                        }, // Gabriel Improve this
                        Throwable::printStackTrace);// Gabriel Notify
    }

    private void updateWidgets(Recipe recipe) {
        Timber.d("Updating with ingredients :: %s", recipe);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, IngredientsWidgetProvider.class)
        );
        IngredientsWidgetProvider.updateAppWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }

    public static void startActionUpdateRecipes(Context context) {
        Intent intent = new Intent(context, IngredientsIntentService.class);
        ContextCompat.startForegroundService(context, intent);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startForeground() {

        String NOTIFICATION_CHANNEL_ID = "com.myrecipes";
        String channelName = "IngredientsIntentService";
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, IMPORTANCE_MIN);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        startForeground(2, notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_recipe_default)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_recipes))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setPriority(PRIORITY_HIGH)
                .setCategory(CATEGORY_SERVICE)
                .build());
    }
}
