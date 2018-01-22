package pl.umk.mat.kacp3r.mobilnabiblioteka.di.modules;

import android.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule
{
    private String mBaseUrl;

    public NetModule(String mBaseUrl)
    {
        this.mBaseUrl = mBaseUrl;
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Application mApplication)
    {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(mApplication.getCacheDir(), cacheSize);

        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();

        return gsonBuilder.serializeNulls().create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache)
    {
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(loggin);
        client.cache(cache);

        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(SimpleXmlConverterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }
}
