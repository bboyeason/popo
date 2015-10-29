package objects;

/**
 * Created by eason on 2015/10/14.
 */
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;

import com.nosae.game.bobo.GameParams;

public class GameObj {

    public enum State {
        step1,
        step2,
        step3,
        step4,
        step5,
        step6,
        step7,
        step8,

    }

    public int destWidth;
    public int destHeight;
    public int halfWidth;
    public int halfHeight;
    public int srcWidth;
    public int srcHeight;
    public Rect srcRect;
    public Rect destRect;
    public Paint paint;
    public int theta;

    public int speed;
    public int alpha;
    public boolean isAlive;
    protected State state = State.step1;

    protected int index = 1;

    // Color Mask Constructor
    public GameObj(int x,int y,int width,int height,int color,int alpha)
    {
        destRect = new Rect(x, y, x + width, y + height);
        paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(alpha);
        this.alpha = alpha;
    }

    public GameObj(int length,int theta,int speed)
    {
        destHeight = length;

        setTheta(theta);
        this.speed = speed;
    }

    public GameObj(int destX,int destY,int destWidth,int destHeight,int srcX,int srcY,int srcWidth,int srcHeight,int speed,int color,int theta)
    {
        this.destWidth = destWidth;
        this.destHeight = destHeight;
        this.halfWidth = (destWidth >> 1);
        this.halfHeight = (destHeight >> 1);
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
        this.destRect = new Rect(destX, destY, destX + destWidth, destY + destHeight);
        this.srcRect = new Rect(srcX, srcY, srcX + srcWidth, srcY + srcHeight);;
        this.speed = speed;
        setTheta(theta);

        // �]�w�C��L�o
        setColorFilter(color);
    }

    // �]�w�C��L�o
    public void setColorFilter(int color)
    {
        paint = new Paint();

        if (color != Color.WHITE)
        {
            PorterDuffColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            paint.setColorFilter(filter);
        }else
            paint.setColorFilter(null);
    }

    public int getX() {
        return destRect.left;
    }

    public int getY()
    {
        return destRect.top;
    }

    protected void setX(int x)
    {
        destRect.left = x;
        destRect.right = x + destWidth;
    }

    public void setY(int y)
    {
        destRect.top = y;
        destRect.bottom = y + destHeight;
    }


    protected void addX(int speed)
    {
        setX(destRect.left + speed);
    }

    public void addY(int speed)
    {
        setY(destRect.top + speed);
    }

/*    public void scale(int addScaleX, int addScaleY) {
        destRect.left -= addScaleX;
        destRect.top -= addScaleY;
        destRect.right += addScaleX;
        destRect.bottom += addScaleY;
    }*/

    public void moveTo(int x, int y) {
        setX(x);
        setY(y);
//        if (Enable) {
//            Rect rect = drawable.getBounds();
//            drawable.setBounds(newLeft, newTop, newLeft + rect.width(), newTop
//                    + rect.height());
//        }
    }

    public void addTheta(int speed)
    {
        setTheta(theta + speed);
    }

    public void setTheta(int theta)
    {
        theta = theta % 360;

        if (theta < 0)
            theta += 360;

        this.theta = theta;
    }

    protected int getTargetTheta(Rect objA,Rect objB)
    {
        int xDistance = (objA.left + (objA.width() >> 1)) - (objB.left + (objB.width() >> 1));
        int yDistance = (objA.top + (objA.height() >> 1)) - (objB.top + (objB.height() >> 1));
        return GameParams.getTheta(xDistance, yDistance);
    }

    protected void move()
    {
        addX((int) (GameParams.Cosine[theta] * speed));
        addY((int) (GameParams.Sine[theta] * speed));
    }

    protected void rotation(int originX,int originY ,int destTheta,int distance)
    {
        setX(originX + (int)(GameParams.Cosine[destTheta] * distance - GameParams.Sine[destTheta] * distance));
        setY(originY + (int)(GameParams.Sine[destTheta] * distance + GameParams.Cosine[destTheta] * distance));
    }

    protected void setAnimationIndex(int col)
    {
        srcRect.left = (index % col) * srcWidth;
        srcRect.top = (index / col) * srcHeight;

        srcRect.right = srcRect.left + srcWidth;
        srcRect.bottom = srcRect.top + srcHeight;
//        DebugConfig.d("index: " + index);
//        DebugConfig.d("setAnimationIndex: " + srcRect.left + ", " + srcRect.top + ", " + srcRect.right + ", " + srcRect.bottom);

    }
    /*
    *//**
     * 物件顯示角度
     *//*
    public float angle;

    *//**
     * 物件影像資源
     *//*
    public Drawable drawable;

    *//**
     * 是否顯示
     *//*
    public boolean Visible = true;

    *//**
     * 控制致能
     *//*
    public boolean Enable = true;

    *//**
     * 暫存的物件位置
     *//*
    private Rect saveRect;

    *//**
     * 暫存的物件角度
     *//*
    public float saveAngle;


    public int speed;
    public int alpha;
    public boolean isAlive;
    public boolean isAnimation;

    // Animation index
    protected int index = 7;
    protected static int srcCol = 1;
    protected static int srcRow = 1;

    public GameObj(Drawable drawable) {
        this.drawable = drawable;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                .getIntrinsicHeight());
        this.save();
    }
    public GameObj(GameObj gameObj,Drawable drawable) {
        this.drawable = drawable;
        this.drawable.setBounds(gameObj.drawable.copyBounds());
        this.angle = gameObj.angle;
        this.save();
    }

    *//**
     * 儲存目前物件狀態
     *//*
    public void save() {
        if (Enable) {
            saveRect = drawable.copyBounds();
            saveAngle = angle;
        }
    }

    *//**
     * 恢復物件狀態
     *//*
    public void restore() {
        if (Enable) {
            drawable.setBounds(saveRect);
            angle = saveAngle;
        }
    }

    *//**
     * 旋轉物件
     *//*
    public void rotate(float angle) {
        if (Enable) {
            this.angle += angle;
            this.angle %= 360;
        }
    }

    *//**
     * 設定物件角度
     *//*
    public void setAngle(float angle) {
        if (Enable) {
            this.angle = angle;
            this.angle %= 360;
        }
    }

    *//**
     * 得到物件角度
     *//*
    public float getAngle(float angle) {
        return angle;
    }

    *//**
     * 移動物件到新的座標點上
     *//*


    *//**
     * 移動物件到新的座標點上
     *//*
    public void moveTo(float newLeft, float newTop) {
        moveTo((int)newLeft,(int)newTop);
    }

    *//**
     * 物件移動一個向量距離
     *//*
    public void move(int dx, int dy) {
        if (Enable) {
            Rect rect = drawable.getBounds();
            drawable.setBounds(rect.left + dx, rect.top + dy, rect.right + dx,
                    rect.bottom + dy);
        }
    }

    *//**
     * 物件移動一個向量距離
     *//*
    public void move(float dx, float dy) {
        move((int)dx,(int)dy);
    }

    *//**
     * 物件範圍縮放
     *//*
    public void scale(int addScaleX, int addScaleY) {
        if (Enable) {
            Rect rect = drawable.getBounds();
            drawable.setBounds(rect.left - addScaleX, rect.top - addScaleY,
                    rect.right + addScaleX, rect.bottom + addScaleY);
        }
    }

    public void draw(Canvas canvas) {
        if (Visible) {
            canvas.save();
            canvas.rotate(angle, drawable.getBounds().centerX(), drawable
                    .getBounds().centerY());
            drawable.draw(canvas);
            canvas.restore();
//            Log.d("GameObj", "=== draw ===");
        }
    }


    *//**
     * 得到物件中心X座標
     *//*
    public int centerX() {
        return drawable.getBounds().centerX();
    }

    *//**
     *得到物件中心Y座標
     *//*
    public int centerY() {
        return drawable.getBounds().centerY();
    }

    *//**
     * 得到物件範圍
     *//*
    public Rect getRect() {
        return drawable.getBounds();
    }

    *//**
     * 得到物件高度
     *//*
    public int getHeight() {
        return drawable.getBounds().height();
    }

    *//**
     * 得到物件寬度
     *//*
    public int getWidth() {
        return drawable.getBounds().width();
    }

    *//**
     * 得到原始影像高度
     *//*
    public int getSrcHeight() {
        return drawable.getIntrinsicHeight();
    }

    *//**
     * 得到原始影像寬度
     *//*
    public int getSrcWidth() {
        return drawable.getIntrinsicWidth();
    }

    *//**
     * 設定物件範圍
     *//*
    public void setRect(Rect rect) {
        drawable.setBounds(rect);
    }

    *//**
     * 設定物件範圍
     *//*
    public void setRect(int left, int top, int right, int bottom) {
        drawable.setBounds(left, top, right, bottom);

    }

    *//**
     * 判斷物件使否與參數範圍相交
     * 當相交時自動調整物件範圍
     *//*
    public boolean intersect(Rect r) {
        return drawable.getBounds().intersect(r);
    }

    *//**
     * 判斷物件使否與參數範圍相交
     * 當相交時自動調整物件範圍
     *//*
    public boolean intersect(GameObj obj) {
        return this.intersect(obj.getRect());
    }

    *//**
     * 判斷物件範圍是否包函與參數範圍
     *//*
    public boolean contains(Rect r) {
        return drawable.getBounds().contains(r);
    }

    *//**
     * 判斷物件範圍是否包函與參數範圍
     *//*
    public boolean contains(GameObj obj) {
        return this.contains(obj.getRect());
    }

    *//**
     * 判斷物件範圍是否包函與參數點
     *//*
    public boolean contains(int x,int y) {
        return drawable.getBounds().contains(x, y);
    }*/
}