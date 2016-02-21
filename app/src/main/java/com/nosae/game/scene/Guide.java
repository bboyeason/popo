package com.nosae.game.scene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.MainActivity;
import com.nosae.game.popo.R;

/**
 * Created by eason on 2015/10/27.
 */
public class Guide extends Activity {
    private static Guide sInstance;

    private static int stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        sInstance = this;

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        Intent intent = getIntent();
        stage = intent.getIntExtra(GameParams.STAGE, 0);
        try {
            switch (stage) {
                case 1:
                    imageView.setImageResource(R.drawable.guide01);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.guide02);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.guide03);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.guide04);
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.guide05);
                    break;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(sInstance, MainActivity.class);
                        i.putExtra(GameParams.STAGE, stage);
                        startActivity(i);
                        this.finish();
                    }

                    private void finish() {
                        //TODO stop music
                    }
                });
            }
        });
    }
}
