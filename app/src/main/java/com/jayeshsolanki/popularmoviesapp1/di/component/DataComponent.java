package com.jayeshsolanki.popularmoviesapp1.di.component;

import com.jayeshsolanki.popularmoviesapp1.di.module.AppModule;
import com.jayeshsolanki.popularmoviesapp1.di.module.DataModule;
import com.jayeshsolanki.popularmoviesapp1.ui.activity.BaseActivity;
import com.jayeshsolanki.popularmoviesapp1.ui.fragment.MovieListFragment;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {DataModule.class, AppModule.class})
public interface DataComponent {
    void inject(MovieListFragment movieListFragment);

    Retrofit retrofit();
    OkHttpClient okHttpClient();
}
