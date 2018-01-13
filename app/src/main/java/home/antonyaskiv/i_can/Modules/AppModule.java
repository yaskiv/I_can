package home.antonyaskiv.i_can.Modules;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import home.antonyaskiv.i_can.Application.App;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */
@Module
public class AppModule {
    private Context AppContext;
    public AppModule(@NonNull Context context)
    {
        this.AppContext=context;
    }
    @Provides
    @Singleton
    Context provideContext()
    {
        return this.AppContext;
    }
}
