package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.database.DBManager;
import com.dhthuyloi.mxhung.dulichvietnam.model.Experience;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MXHung on 11/8/2016.
 */

public class DetailExp extends AppCompatActivity {
    @Bind(R.id.tvExp) TextView tvExp;
    @Bind(R.id.imExp) ImageView imExp;
    @Bind(R.id.tvDetail) TextView tvDetail;

    private int id;
    private DBManager db;
    private Experience experience;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_experience);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DBManager(this);
        id = getIntent().getIntExtra("id", 1);
        experience = db.getExperienceDetail(id);
        getSupportActionBar().setTitle(experience.getName());
//        tvExp.setText(experience.getName());
        tvDetail.setText(Html.fromHtml(experience.getDetail()));
//        PicassoClient.downloadImage(getApplicationContext(), experience.getImage(), imExp);
        Glide.with(getApplicationContext())
                .load(experience.getImage())
                .error(R.drawable.im_thumbnail)
                .skipMemoryCache(true)
                .into(imExp);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return true;
    }
}
