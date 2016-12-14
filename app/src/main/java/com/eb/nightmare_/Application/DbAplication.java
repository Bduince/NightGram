package com.eb.nightmare_.Application;

import android.app.Application;
import android.content.Context;

import com.eb.nightmare_.DB.DbContext;

/**
 * 全局Context对象
 */
public class DbAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!DbContext.isInitialized()) {
            DbContext.init(getApplicationContext());
        }
    }

}
