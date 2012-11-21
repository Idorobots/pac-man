package pac.man;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import pac.man.model.Player;
import pac.man.gfx.Animation;
import pac.man.util.Vector;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int TIME_DELTA = 33;

    MainThread thread;

    Player player;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        thread = new MainThread(getHolder(), this);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO init
        Animation[] animations = new Animation[2];

        animations[0] = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.pacdroid_idle), 1, 1000);
        animations[1] = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.pacdroid_moving), 4, 500);

        player = new Player(new Point(getWidth()/2, 30), animations);
        player.setSpeed(new Vector(0.0, 150.0));
        player.setActiveAnimation(1);

        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            player.handleMove((int) event.getX(), (int) event.getY(), getWidth(), getHeight());
        }

        return true;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        player.draw(canvas);

        // TODO Other Stuff
    }

    public void update(long dt) {
        player.update(dt);

        if(player.isMoving()) {
            player.setActiveAnimation(1);
        }
        else player.setActiveAnimation(0);

        // TODO Stuff
    }
}