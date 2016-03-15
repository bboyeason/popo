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
    RadioGroup radioGroup06;
    RadioGroup radioGroup07;
    RadioGroup radioGroup08;
    RadioGroup radioGroup09;
    RadioGroup radioGroup10;
    RadioGroup radioGroup11;
    private int currentPage = 0;
    private int point = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karnofsky_scale);

        if (GameParams.music == null) {
            GameParams.music = new Music(this, R.raw.stage6, GameParams.musicVolumeRatio);
            GameParams.music.setLooping(true);
            GameParams.music.Play();
        }
        ViewGroup systemContent = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        changeFonts(systemContent);
        final ImageView imageViewInfo = (ImageView) findViewById(R.id.imageViewInfo);
        try {
            imageViewInfo.setImageResource(R.drawable.k_introduction);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewInfo.setImageDrawable(null);
                imageViewInfo.setVisibility(View.GONE);
            }
        });

        getRadioGroupView();
        setRadioGroupListener();
    }

    public void getRadioGroupView() {
        radioGroup01 = (RadioGroup) findViewById(R.id.radioGroup01);
        radioGroup02 = (RadioGroup) findViewById(R.id.radioGroup02);
        radioGroup03 = (RadioGroup) findViewById(R.id.radioGroup03);
        radioGroup04 = (RadioGroup) findViewById(R.id.radioGroup04);
        radioGroup05 = (RadioGroup) findViewById(R.id.radioGroup05);
        radioGroup06 = (RadioGroup) findViewById(R.id.radioGroup06);
        radioGroup07 = (RadioGroup) findViewById(R.id.radioGroup07);
        radioGroup08 = (RadioGroup) findViewById(R.id.radioGroup08);
        radioGroup09 = (RadioGroup) findViewById(R.id.radioGroup09);
        radioGroup10 = (RadioGroup) findViewById(R.id.radioGroup10);
        if (currentPage == 2)
            radioGroup11 = (RadioGroup) findViewById(R.id.radioGroup11);
    }

    public void setRadioGroupListener() {
        radioGroup01.setOnCheckedChangeListener(listener);
        radioGroup02.setOnCheckedChangeListener(listener);
        radioGroup03.setOnCheckedChangeListener(listener);
        radioGroup04.setOnCheckedChangeListener(listener);
        radioGroup05.setOnCheckedChangeListener(listener);
        radioGroup06.setOnCheckedChangeListener(listener);
        radioGroup07.setOnCheckedChangeListener(listener);
        radioGroup08.setOnCheckedChangeListener(listener);
        radioGroup09.setOnCheckedChangeListener(listener);
        radioGroup10.setOnCheckedChangeListener(listener);
        if (currentPage == 2)
            radioGroup11.setOnCheckedChangeListener(listener);
    }

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            nextPage();
        }
    };
    private void nextPage() {
        DebugConfig.d("************** nextPage *************** current page: " + currentPage);

        if (radioGroup01.getCheckedRadioButtonId() != -1
                && radioGroup02.getCheckedRadioButtonId() != -1
                && radioGroup03.getCheckedRadioButtonId() != -1
                && radioGroup04.getCheckedRadioButtonId() != -1
                && radioGroup05.getCheckedRadioButtonId() != -1
                && radioGroup06.getCheckedRadioButtonId() != -1
                && radioGroup07.getCheckedRadioButtonId() != -1
                && radioGroup08.getCheckedRadioButtonId() != -1
                && radioGroup09.getCheckedRadioButtonId() != -1
                && radioGroup10.getCheckedRadioButtonId() != -1) {
            if (currentPage == 3 && radioGroup11.getCheckedRadioButtonId() != -1) {
                DebugConfig.d("************** result ***************");
                //TODO show result
                radioGroup01.setOnCheckedChangeListener(null);
                radioGroup02.setOnCheckedChangeListener(null);
                radioGroup03.setOnCheckedChangeListener(null);
                radioGroup04.setOnCheckedChangeListener(null);
                radioGroup05.setOnCheckedChangeListener(null);
                radioGroup06.setOnCheckedChangeListener(null);
                radioGroup07.setOnCheckedChangeListener(null);
                radioGroup08.setOnCheckedChangeListener(null);
                radioGroup09.setOnCheckedChangeListener(null);
                radioGroup10.setOnCheckedChangeListener(null);
                radioGroup11.setOnCheckedChangeListener(null);
                Intent i;
                i = new Intent(this, KarnofskyScaleResult.class);
                i.putExtra(GameParams.RESULT, point);
                startActivity(i);
                this.finish();
            }  else {
                if (currentPage <= 2)
                    currentPage++;
                switch (currentPage) {
                    case 1:
                        setContentView(R.layout.karnofsky_scale_01);
                        break;
                    case 2:
                        setContentView(R.layout.karnofsky_scale_02);
                        break;
                }
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
        } else if (radioGroup01.getCheckedRadioButtonId() == R.id.radioButton01_04) {
            point += 3;
        }

        if (radioGroup02.getCheckedRadioButtonId() == R.id.radioButton02_01) {
            point += 0;
        } else if (radioGroup02.getCheckedRadioButtonId() == R.id.radioButton02_02) {
            point += 1;
        } else if (radioGroup02.getCheckedRadioButtonId() == R.id.radioButton02_03) {
            point += 2;
        } else if (radioGroup02.getCheckedRadioButtonId() == R.id.radioButton02_04) {
            point += 3;
        }

        if (radioGroup03.getCheckedRadioButtonId() == R.id.radioButton03_01) {
            point += 0;
        } else if (radioGroup03.getCheckedRadioButtonId() == R.id.radioButton03_02) {
            point += 1;
        } else if (radioGroup03.getCheckedRadioButtonId() == R.id.radioButton03_03) {
            point += 2;
        } else if (radioGroup03.getCheckedRadioButtonId() == R.id.radioButton03_04) {
            point += 3;
        }

        if (radioGroup04.getCheckedRadioButtonId() == R.id.radioButton04_01) {
            point += 0;
        } else if (radioGroup04.getCheckedRadioButtonId() == R.id.radioButton04_02) {
            point += 1;
        } else if (radioGroup04.getCheckedRadioButtonId() == R.id.radioButton04_03) {
            point += 2;
        } else if (radioGroup04.getCheckedRadioButtonId() == R.id.radioButton04_04) {
            point += 3;
        }

        if (radioGroup05.getCheckedRadioButtonId() == R.id.radioButton05_01) {
            point += 0;
        } else if (radioGroup05.getCheckedRadioButtonId() == R.id.radioButton05_02) {
            point += 1;
        } else if (radioGroup05.getCheckedRadioButtonId() == R.id.radioButton05_03) {
            point += 2;
        } else if (radioGroup05.getCheckedRadioButtonId() == R.id.radioButton05_04) {
            point += 3;
        }

        if (radioGroup06.getCheckedRadioButtonId() == R.id.radioButton06_01) {
            point += 0;
        } else if (radioGroup06.getCheckedRadioButtonId() == R.id.radioButton06_02) {
            point += 1;
        } else if (radioGroup06.getCheckedRadioButtonId() == R.id.radioButton06_03) {
            point += 2;
        } else if (radioGroup06.getCheckedRadioButtonId() == R.id.radioButton06_04) {
            point += 3;
        }

        if (radioGroup07.getCheckedRadioButtonId() == R.id.radioButton07_01) {
            point += 0;
        } else if (radioGroup07.getCheckedRadioButtonId() == R.id.radioButton07_02) {
            point += 1;
        } else if (radioGroup07.getCheckedRadioButtonId() == R.id.radioButton07_03) {
            point += 2;
        } else if (radioGroup07.getCheckedRadioButtonId() == R.id.radioButton07_04) {
            point += 3;
        }

        if (radioGroup08.getCheckedRadioButtonId() == R.id.radioButton08_01) {
            point += 0;
        } else if (radioGroup08.getCheckedRadioButtonId() == R.id.radioButton08_02) {
            point += 1;
        } else if (radioGroup08.getCheckedRadioButtonId() == R.id.radioButton08_03) {
            point += 2;
        } else if (radioGroup08.getCheckedRadioButtonId() == R.id.radioButton08_04) {
            point += 3;
        }

        if (radioGroup09.getCheckedRadioButtonId() == R.id.radioButton09_01) {
            point += 0;
        } else if (radioGroup09.getCheckedRadioButtonId() == R.id.radioButton09_02) {
            point += 1;
        } else if (radioGroup09.getCheckedRadioButtonId() == R.id.radioButton09_03) {
            point += 2;
        } else if (radioGroup09.getCheckedRadioButtonId() == R.id.radioButton09_04) {
            point += 3;
        }

        if (radioGroup10.getCheckedRadioButtonId() == R.id.radioButton10_01) {
            point += 0;
        } else if (radioGroup10.getCheckedRadioButtonId() == R.id.radioButton10_02) {
            point += 1;
        } else if (radioGroup10.getCheckedRadioButtonId() == R.id.radioButton10_03) {
            point += 2;
        } else if (radioGroup10.getCheckedRadioButtonId() == R.id.radioButton10_04) {
            point += 3;
        }

        if (radioGroup11 != null) {
            if (radioGroup11.getCheckedRadioButtonId() == R.id.radioButton11_01) {
                point += 0;
            } else if (radioGroup11.getCheckedRadioButtonId() == R.id.radioButton11_02) {
                point += 1;
            } else if (radioGroup11.getCheckedRadioButtonId() == R.id.radioButton11_03) {
                point += 2;
            } else if (radioGroup11.getCheckedRadioButtonId() == R.id.radioButton11_04) {
                point += 3;
            }
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
