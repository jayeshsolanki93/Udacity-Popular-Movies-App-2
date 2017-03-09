package com.jayeshsolanki.popularmoviesapp1.ui.activity;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jayeshsolanki.popularmoviesapp1.PopularMoviesApp;
import com.jayeshsolanki.popularmoviesapp1.di.component.AppComponent;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {

    @Inject
    Application mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getAppComponent().inject(this);
    }

    protected AppComponent getAppComponent() {
        return  ((PopularMoviesApp) getApplication()).getAppComponent();
    }

}
