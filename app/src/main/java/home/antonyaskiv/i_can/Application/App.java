package home.antonyaskiv.i_can.Application;

import android.app.Application;

import com.indoorway.android.common.sdk.IndoorwaySdk;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import home.antonyaskiv.i_can.Component.AppComponent;
import home.antonyaskiv.i_can.Component.DaggerAppComponent;
import home.antonyaskiv.i_can.Modules.AppModule;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class App extends Application {
    private static AppComponent appComponent;
    public static AppComponent getAppComponent()
    {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        appComponent=buildComponent();
        IndoorwaySdk.initContext(this);


        IndoorwaySdk.configure("466b674b-ebd5-4db9-98ba-456d242afbf4");
    }

    protected AppComponent buildComponent()
    {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
