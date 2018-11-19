package com.rjp.memorygame;

import android.app.Application;

/**
 * author : Gimpo create on 2018/11/19 15:10
 * email  : jimbo922@163.com
 */
public class App extends Application {

    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
    }

    public static Application getInstance(){
        return app;
    }
}
