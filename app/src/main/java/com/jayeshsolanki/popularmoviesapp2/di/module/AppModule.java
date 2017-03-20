package com.jayeshsolanki.popularmoviesapp2.di.module;

import android.app.Application;

import com.jayeshsolanki.popularmoviesapp2.PopularMoviesApp;

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
