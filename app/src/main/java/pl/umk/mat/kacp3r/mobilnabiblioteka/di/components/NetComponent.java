package pl.umk.mat.kacp3r.mobilnabiblioteka.di.components;

import javax.inject.Singleton;

import dagger.Component;
import pl.umk.mat.kacp3r.mobilnabiblioteka.di.modules.AppModule;
import pl.umk.mat.kacp3r.mobilnabiblioteka.di.modules.NetModule;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book.AboutBookActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ToReadFragment;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.search.SearchActivity;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent
{
    void inject(SearchActivity searchActivity);
    void inject(AboutBookActivity aboutBookActivity);
}
