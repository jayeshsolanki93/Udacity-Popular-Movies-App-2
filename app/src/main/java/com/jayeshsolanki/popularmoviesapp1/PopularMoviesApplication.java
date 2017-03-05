package com.jayeshsolanki.popularmoviesapp1;

import android.app.Application;

import com.jayeshsolanki.popularmoviesapp1.di.component.AppComponent;
import com.jayeshsolanki.popularmoviesapp1.di.component.DaggerAppComponent;
import com.jayeshsolanki.popularmoviesapp1.di.component.DaggerDataComponent;
import com.jayeshsolanki.popularmoviesapp1.di.component.DataComponent;
import com.jayeshsolanki.popularmoviesapp1.di.module.AppModule;
import com.jayeshsolanki.popularmoviesapp1.di.module.DataModule;

import timber.log.Timber;

public class PopularMoviesApplication extends Application {

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
                .dataModule(new DataModule("https://api.themoviedb.org/3/"))
                .build();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    public DataComponent getDataComponent() {
        return this.dataComponent;
    }

}
