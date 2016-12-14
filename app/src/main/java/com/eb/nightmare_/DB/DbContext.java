package com.eb.nightmare_.DB;

import android.content.Context;


public class DbContext {
    private static DbContext instance;
    private Context applicationContext;

    public static DbContext getInstance() {
        if (instance == null){
            throw new RuntimeException(DbContext.class.getSimpleName() + "has not been initialized!");
        }
        return instance;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public DbContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 全局信息 只能调用一次
     */
    public static void init(Context applicationContext) {
        if (instance != null) {
            throw new RuntimeException(DbContext.class.getSimpleName() + " can not be initialized multiple times!");
        }
        instance = new DbContext(applicationContext);
    }

    public static boolean isInitialized() {
        if(instance == null){
            return false;
        }
        else{
            return  true;
        }
    }

}
