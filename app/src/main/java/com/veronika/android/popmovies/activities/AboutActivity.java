package com.veronika.android.popmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.veronika.android.popmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by veronika on 17.01.18.
 */

public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.tv_privacy_label)TextView mPrivacyTitle;
    @BindView(R.id.tv_privacy_content)TextView mPrivacyContent;
    @BindView(R.id.imageTheMovieDB)ImageView mMovieDBLogo;
    @BindView(R.id.tv_data_collection) TextView mDataCollection;
    @BindView(R.id.tv_link_to_themoviedb) TextView mLinkToMovieDb;
    @BindView(R.id.tv_email_to_me) TextView mEmailTV;
    @BindView(R.id.grayline)View mGrayLine;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
    }
}
