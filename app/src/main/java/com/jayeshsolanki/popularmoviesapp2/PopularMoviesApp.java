package com.jayeshsolanki.popularmoviesapp2;

import android.app.Application;

import com.jayeshsolanki.popularmoviesapp2.di.component.AppComponent;
import com.jayeshsolanki.popularmoviesapp2.di.component.DaggerAppComponent;
import com.jayeshsolanki.popularmoviesapp2.di.component.DaggerDataComponent;
import com.jayeshsolanki.popularmoviesapp2.di.component.DataComponent;
import com.jayeshsolanki.popularmoviesapp2.di.module.AppModule;
import com.jayeshsolanki.popularmoviesapp2.di.module.DataModule;

import timber.log.Timber;

public class PopularMoviesApp extends Application {

    private AppComponent appComponent;
    private DataComponent dataComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        this.initializeInjector();

        Timber.plant(new Timber.DebugTree());
    }

    private void initializeInjector() {
        AppModule appModule = new AppModule(this);
        this.appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();
        this.dataComponent = DaggerDataComponent.builder()
                .appModule(appModule)
                .dataModule(new DataModule(AppConstants.API_URL))
                .build();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    public DataComponent getDataComponent() {
        return this.dataComponent;
    }

}
