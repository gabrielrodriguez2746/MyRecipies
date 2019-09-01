package com.myrecipes;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListWidgetsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }
}
