package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.database.DBManager;
import com.dhthuyloi.mxhung.dulichvietnam.model.Introduce;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MXHung on 9/8/2016.
 */
public class IntroduceActivity extends AppCompatActivity{
	private int id_diadanh;
	DBManager db;
	private Introduce introduce;
	@Bind(R.id.tvIntro) TextView tvIntro;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ButterKnife.bind(this);
		id_diadanh = getIntent().getIntExtra("id_diadanh", 0);
		db = new DBManager(this);
		introduce = db.getIntroID(id_diadanh);
		tvIntro.setText(Html.fromHtml(introduce.getIntro()));

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
