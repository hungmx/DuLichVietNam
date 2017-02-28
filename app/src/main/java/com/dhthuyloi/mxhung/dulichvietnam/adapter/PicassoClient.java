package com.dhthuyloi.mxhung.dulichvietnam.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.squareup.picasso.Picasso;

/**
 * Created by trung_000 on 5/9/2016.
 */
public class PicassoClient {
    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.im_thumbnail).into(img);
        }else
        {
            Picasso.with(c).load(R.drawable.im_thumbnail).into(img);
        }
    }
}
