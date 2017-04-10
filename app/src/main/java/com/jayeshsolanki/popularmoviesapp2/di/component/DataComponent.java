package com.jayeshsolanki.popularmoviesapp2.di.component;

import com.jayeshsolanki.popularmoviesapp2.di.module.AppModule;
import com.jayeshsolanki.popularmoviesapp2.di.module.DataModule;
import com.jayeshsolanki.popularmoviesapp2.ui.activity.MovieListActivity;
import com.jayeshsolanki.popularmoviesapp2.ui.fragment.MovieFragment;
import com.jayeshsolanki.popularmoviesapp2.ui.fragment.MovieListFragment;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {DataModule.class, AppModule.class})
public interface DataComponent {
    void inject(MovieListFragment movieListFragment);

    void inject(MovieListActivity movieListActivity);

    void inject(MovieFragment movieFragment);

    Retrofit retrofit();
}
