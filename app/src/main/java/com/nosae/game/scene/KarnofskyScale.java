package com.nosae.game.scene;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nosae.game.popo.R;

/**
 * Created by eason on 2015/10/27.
 */
public class KarnofskyScale extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karnofsky_scale);
        ImageView imageViewInfo = (ImageView) findViewById(R.id.imageViewInfo);
        try {
            imageViewInfo.setImageResource(R.drawable.k_introduction);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
