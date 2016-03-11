package com.nosae.game.scene;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class KarnofskyScaleResult extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karnofsky_scale_result);
        int resultPoint = 0;
        Intent intent = getIntent();
        resultPoint = intent.getIntExtra(GameParams.RESULT, 0);
        final ImageView imageViewResult = (ImageView) findViewById(R.id.imageViewResult);
        try {
            if (resultPoint <= 28) {
                imageViewResult.setImageResource(R.drawable.result_01);
            } else if (resultPoint >= 29 && resultPoint <= 35) {
                imageViewResult.setImageResource(R.drawable.result_02);
            } else if (resultPoint >= 36 && resultPoint <= 51) {
                imageViewResult.setImageResource(R.drawable.result_03);
            } else if (resultPoint >= 52) {
                imageViewResult.setImageResource(R.drawable.result_04);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

    }

}
