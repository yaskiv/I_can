package home.antonyaskiv.i_can.Modules;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */
@Module
public class ImplMapFragmentPresenterModule {
    @Provides
    @NonNull
    @Singleton
    public ImplMapFragmentPresenter provideWhenceFragmentPresentrUtils(Context context)
    {
        return new ImplMapFragmentPresenter(context);
    }
}
