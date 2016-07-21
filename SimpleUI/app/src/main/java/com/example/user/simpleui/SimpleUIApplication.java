package com.example.user.simpleui;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by user on 2016/7/19.
 */
public class SimpleUIApplication extends Application {
    @Override
    public void  onCreate(){
        super.onCreate();

        ParseObject.registerSubclass(Order.class); //標明此物件是可以上傳的
        ParseObject.registerSubclass(Drink.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("xDTUNne92ZNeldPcTrUPMT9RUowQwC7KxR7aLGx7")
                        .server("https://parseapi.back4app.com/")
                        .clientKey("FGYIC6Z7VTMLFMyWo1RUoMNNT7WUePa5ngxXCLSr")
                        .enableLocalDataStore()  //開啟local database的功能，沒有網路的情況下亦可儲存
                        .build()

        );
    }

}
