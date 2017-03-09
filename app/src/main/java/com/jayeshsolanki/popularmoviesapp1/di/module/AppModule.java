package com.jayeshsolanki.popularmoviesapp1.di.module;

import android.app.Application;

import com.jayeshsolanki.popularmoviesapp1.PopularMoviesApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final PopularMoviesApp app;

    public AppModule(PopularMoviesApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.app;
    }

}
