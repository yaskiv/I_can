package home.antonyaskiv.i_can.Component;

import javax.inject.Singleton;

import dagger.Component;
import home.antonyaskiv.i_can.Modules.AppModule;
import home.antonyaskiv.i_can.Modules.ImplMapFragmentPresenterModule;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;
import home.antonyaskiv.i_can.View.Dialogs.AddDialog;
import home.antonyaskiv.i_can.View.Dialogs.EditDialog;
import home.antonyaskiv.i_can.View.MapFragment;
import home.antonyaskiv.i_can.View.MyPostsFragment;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */
@Component(modules = {ImplMapFragmentPresenterModule.class, AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MapFragment mapFragment);
    void inject(MyPostsFragment myPostsFragment);
    void inject(AddDialog addDialog);
    void inject(EditDialog editDialog);
}
