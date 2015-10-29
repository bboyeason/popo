package objects;

import android.graphics.Color;

import com.nosae.game.bobo.GameParams;
import com.nosae.game.bobo.Text;

/**
 * Created by eason on 2015/10/27.
 */
public class ColorMask extends GameObj{

        private int startFrame;
        private int delayFrame = 300;
        public Text warningText;

        public ColorMask(int color,int alpha) {
            super(0, 0, GameParams.scaleWidth, GameParams.scaleHeight, color, alpha);

            warningText = new Text(GameParams.halfWidth - 100, GameParams.halfHeight, 36, "Game Over", Color.BLACK);
        }

        public ColorMask(int x, int y, int destWidth, int destHeight, int color,int alpha) {
            super(x, y, destWidth, destHeight, color, alpha);
        }

        public void Action(int frameTime)
        {
            switch(state)
            {
                case step1:
                    if (!isAlive)
                        isAlive = true;
                    startFrame = frameTime;
                    state = State.step2;

                    break;
                case step2:
                    if (FadeIn())
                        state = State.step3;

                    break;
                case step3:
                    if (FadeOut())
                        state = State.step2;

                    if (frameTime - startFrame > delayFrame)
                    {
                        isAlive = false;
                    }

                    break;
            }
        }

        public boolean FadeIn()
        {
            alpha += 5;
            if (alpha > 192)
            {
                alpha = 192;
//                GameParams.playSound(Sound.alert);
                return true;
            }

            paint.setAlpha(alpha);
            return false;
        }

        public boolean FadeOut()
        {
            alpha -= 5;
            if (alpha < 0)
            {
                alpha = 0;
                return true;
            }

            paint.setAlpha(alpha);
            return false;
        }
    }
