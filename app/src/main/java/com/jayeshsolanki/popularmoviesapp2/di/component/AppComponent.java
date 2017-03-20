package com.jayeshsolanki.popularmoviesapp2.di.component;

import android.app.Application;

import com.jayeshsolanki.popularmoviesapp2.di.module.AppModule;
import com.jayeshsolanki.popularmoviesapp2.ui.activity.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(BaseActivity baseActivity);

    Application context();
}
