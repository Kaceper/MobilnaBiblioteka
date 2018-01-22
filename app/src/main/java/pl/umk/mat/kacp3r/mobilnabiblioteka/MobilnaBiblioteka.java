package pl.umk.mat.kacp3r.mobilnabiblioteka;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.umk.mat.kacp3r.mobilnabiblioteka.di.components.DaggerNetComponent;
import pl.umk.mat.kacp3r.mobilnabiblioteka.di.components.NetComponent;
import pl.umk.mat.kacp3r.mobilnabiblioteka.di.modules.AppModule;
import pl.umk.mat.kacp3r.mobilnabiblioteka.di.modules.NetModule;

public class MobilnaBiblioteka extends Application
{
    private NetComponent netComponent;

    public NetComponent getNetComponent()
    {
        return netComponent;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(getString(R.string.backend_server)))
                .build();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Override
    public void onTerminate()
    {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }
}
