package pac.man.gfx;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Point;

public class Animation {
    private Bitmap bitmap;
    private int currentFrame = 0;
    private int numFrames;
    private long frameTicker;
    private int framePeriod;
    private int spriteWidth;
    private int spriteHeight;
    private Rect sourceRect;

    public Animation(Bitmap bitmap, int numFrames, int period) {
        this.bitmap = bitmap;
        this.spriteWidth = bitmap.getWidth()/numFrames;
        this.spriteHeight = bitmap.getHeight();
        this.numFrames = numFrames;
        this.framePeriod = period/numFrames;

        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
    }

    public void draw(Point position, Canvas canvas) {
        Rect destRect = new Rect(position.x, position.y,
                                 position.x + spriteWidth, position.y + spriteHeight);
        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
    }

    public void update(long dt) {
        frameTicker += dt;

        if(frameTicker >= framePeriod) {
            frameTicker -= framePeriod;
            currentFrame = (currentFrame + 1) % numFrames;
        }

        sourceRect.left = currentFrame * spriteWidth;
        sourceRect.right = sourceRect.left + spriteWidth;
    }


    public void reset() {
        currentFrame = 0;
        frameTicker = 0;
    }

    public int getWidth() {
        return spriteWidth;
    }

    public int getHeight() {
        return spriteHeight;
    }
}