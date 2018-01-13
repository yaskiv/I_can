package home.antonyaskiv.i_can.Application;

import android.app.Application;

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
        appComponent=buildComponent();
    }

    protected AppComponent buildComponent()
    {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
