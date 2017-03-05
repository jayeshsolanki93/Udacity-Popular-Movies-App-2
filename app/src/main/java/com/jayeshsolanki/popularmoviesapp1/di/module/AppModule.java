package com.jayeshsolanki.popularmoviesapp1.di.module;

import android.app.Application;

import com.jayeshsolanki.popularmoviesapp1.PopularMoviesApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final PopularMoviesApplication app;

    public AppModule(PopularMoviesApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.app;
    }

}
