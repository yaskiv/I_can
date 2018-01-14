package home.antonyaskiv.i_can.Component;

import javax.inject.Singleton;

import dagger.Component;
import home.antonyaskiv.i_can.Modules.AppModule;
import home.antonyaskiv.i_can.Modules.ImplMapFragmentPresenterModule;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;
import home.antonyaskiv.i_can.View.MapFragment;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */
@Component(modules = {ImplMapFragmentPresenterModule.class, AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MapFragment mapFragment);
}
