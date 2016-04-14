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

import com.nosae.game.objects.Music;
import com.nosae.game.popo.GameParams;
import com.nosae.game.popo.R;
import com.nosae.game.settings.DebugConfig;

/**
 * Created by eason on 2015/10/27.
 */
public class KarnofskyScale extends Activity {
    RadioGroup radioGroup01;
    RadioGroup radioGroup02;
    RadioGroup radioGroup03;
    RadioGroup radioGroup04;
    RadioGroup radioGroup05;
    private int currentPage = 1;
    private int point = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karnofsky_scale_00);

        if (GameParams.music == null) {
            GameParams.music = new Music(this, R.raw.stage6, GameParams.musicVolumeRatio);
            GameParams.music.setLooping(true);
            GameParams.music.Play();
        }
        ViewGroup systemContent = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        changeFonts(systemContent);
        final ImageView imageViewInfo = (ImageView) findViewById(R.id.imageViewInfo);

        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewInfo.setImageDrawable(null);
                imageViewInfo.setVisibility(View.GONE);
                setContentView(R.layout.karnofsky_scale_01);
                getRadioGroupView();
                setRadioGroupListener();
            }
        });

    }

    public void getRadioGroupView() {
        radioGroup01 = (RadioGroup) findViewById(R.id.radioGroup01);
        radioGroup02 = (RadioGroup) findViewById(R.id.radioGroup02);
        radioGroup03 = (RadioGroup) findViewById(R.id.radioGroup03);
        radioGroup04 = (RadioGroup) findViewById(R.id.radioGroup04);
        radioGroup05 = (RadioGroup) findViewById(R.id.radioGroup05);
    }

    public void setRadioGroupListener() {
        radioGroup01.setOnCheckedChangeListener(listener);
        radioGroup02.setOnCheckedChangeListener(listener);
        radioGroup03.setOnCheckedChangeListener(listener);
        radioGroup04.setOnCheckedChangeListener(listener);
        radioGroup05.setOnCheckedChangeListener(listener);
    }

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            nextPage();
        }
    };
    private void nextPage() {

        if (radioGroup01.getCheckedRadioButtonId() != -1
                && radioGroup02.getCheckedRadioButtonId() != -1
                && radioGroup03.getCheckedRadioButtonId() != -1
                && radioGroup04.getCheckedRadioButtonId() != -1
                && radioGroup05.getCheckedRadioButtonId() != -1) {
            if (currentPage == 3) {
                countPoint();
                DebugConfig.d("************** result ***************");
                radioGroup01.setOnCheckedChangeListener(null);
                radioGroup02.setOnCheckedChangeListener(null);
                radioGroup03.setOnCheckedChangeListener(null);
                radioGroup04.setOnCheckedChangeListener(null);
                radioGroup05.setOnCheckedChangeListener(null);
                Intent i;
                i = new Intent(this, KarnofskyScaleResult.class);
                i.putExtra(GameParams.RESULT, point);
                startActivity(i);
                this.finish();
            }  else {
                if (currentPage <= 2)
                    currentPage++;
                switch (currentPage) {
                    case 2:
                        setContentView(R.layout.karnofsky_scale_02);
                        break;
                    case 3:
                        setContentView(R.layout.karnofsky_scale_03);
                        break;
                }
                DebugConfig.d("************** nextPage: " + currentPage + " ***************");
                countPoint();
                getRadioGroupView();
                setRadioGroupListener();
            }
        }
    }

    public void countPoint() {
        if (radioGroup01.getCheckedRadioButtonId() == R.id.radioButton01_01) {
            point += 0;
        } else if (radioGroup01.getCheckedRadioButtonId() == R.id.radioButton01_02) {
            point += 1;
        } else if (radioGroup01.getCheckedRadioButtonId() == R.id.radioButton01_03) {
            point += 2;
        }

        if (radioGroup02.getCheckedRadioButtonId() == R.id.radioButton02_01) {
            point += 0;
        } else if (radioGroup02.getCheckedRadioButtonId() == R.id.radioButton02_02) {
            point += 1;
        } else if (radioGroup02.getCheckedRadioButtonId() == R.id.radioButton02_03) {
            point += 2;
        }

        if (radioGroup03.getCheckedRadioButtonId() == R.id.radioButton03_01) {
            point += 0;
        } else if (radioGroup03.getCheckedRadioButtonId() == R.id.radioButton03_02) {
            point += 1;
        } else if (radioGroup03.getCheckedRadioButtonId() == R.id.radioButton03_03) {
            point += 2;
        }

        if (radioGroup04.getCheckedRadioButtonId() == R.id.radioButton04_01) {
            point += 0;
        } else if (radioGroup04.getCheckedRadioButtonId() == R.id.radioButton04_02) {
            point += 1;
        } else if (radioGroup04.getCheckedRadioButtonId() == R.id.radioButton04_03) {
            point += 2;
        }

        if (radioGroup05.getCheckedRadioButtonId() == R.id.radioButton05_01) {
            point += 0;
        } else if (radioGroup05.getCheckedRadioButtonId() == R.id.radioButton05_02) {
            point += 1;
        } else if (radioGroup05.getCheckedRadioButtonId() == R.id.radioButton05_03) {
            point += 2;
        }

        DebugConfig.d("total point = " + point);
    }
    public void changeFonts(ViewGroup root) {

        Typeface font = Typeface.createFromAsset(getAssets(),
                "fonts/DFGirl-W7-HK-BF.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
//            } else if (v instanceof Button) {
//                ((Button) v).setTypeface(font);
//            } else if (v instanceof EditText) {
//                ((EditText) v).setTypeface(font);
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (GameParams.music != null)
            GameParams.music.player.stop();
    }
}
