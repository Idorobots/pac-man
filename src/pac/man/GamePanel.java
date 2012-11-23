package pac.man;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import pac.man.model.Level;
import pac.man.model.Player;
import pac.man.gfx.Animation;
import pac.man.util.Vector;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int TIME_DELTA = 10; //ms
    public static final int SPEED_GAIN = 100; //pixels per second.

    MainThread thread;

    Player player;
    Level level;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        
        init();
        
        System.out.println("GamePanel");
        
        thread = new MainThread(getHolder(), this);
        thread.start();
    }
    
    private void init() {
        // TODO Implement relevant factories to do this crap here.

        level = new Level(BitmapFactory.decodeResource(getResources(), R.raw.test_level),
                         getWidth(), getHeight());

        Animation[] animations = new Animation[6];

        animations[0] = new Animation(BitmapFactory.decodeResource(getResources(),
                                      R.drawable.idle), 1, 1000);
        animations[1] = new Animation(BitmapFactory.decodeResource(getResources(),
                                      R.drawable.right), 4, 500);
        animations[2] = new Animation(BitmapFactory.decodeResource(getResources(),
                                      R.drawable.up), 4, 500);
        animations[3] = new Animation(BitmapFactory.decodeResource(getResources(),
                                      R.drawable.left), 4, 500);
        animations[4] = new Animation(BitmapFactory.decodeResource(getResources(),
                                      R.drawable.down), 4, 500);
        animations[5] = animations[0];

        Rect r = level.randomPlayerSpawn();
        Vector pos;

        if(r == null) pos = new Vector(getWidth()/2, getHeight()/2);
        else          pos = new Vector(r.left, r.top);

        player = new Player(pos, animations);

        player.setSpeed(new Vector(0.0, 100.0));
        player.setActiveAnimation(1);        
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surface destroyed");
        thread.setRunning(false);
        
        // INFO: Jeśli chcemy ubić proces, to tylko w głównej Activity, w metodzie onDestroy().
//      boolean retry = true;
//      while (retry) {
//          try {
//              thread.join();
//              retry = false;
//          } catch (InterruptedException e) {
//          }
//      }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            Vector ppos = player.getPosition();
            Vector psize = player.getSize();
            Vector touch = new Vector(event.getX(), event.getY());

            Vector direction = new Vector(touch.x - ppos.x - psize.x/2, touch.y - ppos.y - psize.y/2);

            direction.normalize();
            direction.scale(SPEED_GAIN);

            // TODO Pass normalized direction maby?
            player.handleMove(direction);

            direction = player.getSpeed();
        }

        return true;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        level.draw(canvas);
        player.draw(canvas);
    }

    public void update(long dt, Canvas canvas) {
        player.update(dt, canvas);
        level.update(dt, canvas, player);
        // NOTE Since level gets to modify player it should be updated _after_ the player.
    }
}