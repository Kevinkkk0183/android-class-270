package com.example.user.simpleui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by user on 2016/7/19.
 */
public class SimpleUIApplication extends Application {
    @Override
    public void  onCreate(){
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("xDTUNne92ZNeldPcTrUPMT9RUowQwC7KxR7aLGx7")
                        .server("https://parseapi.back4app.com/")
                        .clientKey("FGYIC6Z7VTMLFMyWo1RUoMNNT7WUePa5ngxXCLSr")
                        .build()

        );
    }

}
