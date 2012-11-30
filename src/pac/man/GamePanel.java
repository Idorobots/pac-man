package pac.man;

import java.util.EnumMap;
import java.util.Map;

import pac.man.SoundManager.Sound;
import pac.man.gfx.Animation;
import pac.man.model.Character;
import pac.man.model.Character.AnimationType;
import pac.man.model.Level;
import pac.man.model.Player;
import pac.man.model.Strict4WayMovement;
import pac.man.util.Vector;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    MainThread thread;
    Player player;
    Level level;

    boolean initalized = false;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        thread = new MainThread(getHolder(), this);
        thread.start();
    }

    private void init() {
        initalized = true;
        // TODO Implement relevant factories to do this crap here.

        level = new Level(BitmapFactory.decodeResource(getResources(), R.raw.test_level), getWidth(), getHeight());

        Map<Character.AnimationType, Animation> animations = new EnumMap<Character.AnimationType, Animation>(
                Character.AnimationType.class);

        animations.put(AnimationType.IDLE, new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.idle),
                1, 1000));

//        animations.put(AnimationType.RIGHT,
//                new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.right), 4, 500));
//        animations.put(AnimationType.UP, new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.up), 4,
//                500));
//        animations.put(AnimationType.LEFT, new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.left),
//                4, 500));
//        animations.put(AnimationType.DOWN, new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.down),
//                4, 500));

        // Test animacji duszka.
        animations.put(AnimationType.RIGHT,
                new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.red_right), 2, 500));
        animations.put(AnimationType.UP, new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.red_up), 2,
                500));
        animations.put(AnimationType.LEFT, new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.red_left),
                2, 500));
        animations.put(AnimationType.DOWN, new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.red_down),
                2, 500));

        animations.put(AnimationType.DEATH, new Animation(
                BitmapFactory.decodeResource(getResources(), R.drawable.idle), 1, 1000));

        Rect r = level.randomPlayerSpawn();
        Vector pos;

        if (r == null)
            pos = new Vector(getWidth() / 2, getHeight() / 2);
        else
            pos = new Vector(r.left, r.top);

        player = new Player(pos, animations);
        player.setMovementAlgorithm(new Strict4WayMovement());
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!initalized) {
            init();
            // thread.setRunning(true);
        }

        redraw();
    }

    private void redraw() {
        // Just repaint the board and wait for the user to resume the game.
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            synchronized (getHolder()) {
                draw(canvas);
            }
        } finally {
            if (canvas != null)
                getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRunning(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!thread.getRunning())
            return true;

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            Vector ppos = player.getPosition();
            Vector psize = player.getSize();
            Vector touch = new Vector(event.getX(), event.getY());

            Vector direction = new Vector(touch.x - ppos.x - psize.x / 2, touch.y - ppos.y - psize.y / 2);

            direction.normalize();
            player.handleMove(direction);

            // XXX: test
            // SoundManager.play(Sound.SAMPLE1);
        }

        return true;
    }

    public MainThread getThread() {
        return thread;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        level.draw(canvas);
        player.draw(canvas);
    }

    public void update(long dt, Canvas canvas) {
        player.update(dt, canvas);
        level.update(dt, canvas, player);
        // NOTE Since level gets to modify player it should be updated _after_
        // the player.
    }

    public void restartLevel() {
        thread.setRunning(false);

        Rect r = level.randomPlayerSpawn();
        Vector pos;

        pos = (r == null) ? new Vector(getWidth() / 2, getHeight() / 2) : new Vector(r.left, r.top);

        player.setPosition(pos);
        player.setMovementAlgorithm(new Strict4WayMovement());
        player.setActiveAnimation(AnimationType.IDLE);
        player.setSpeed(new Vector(0, 0));

        thread.setRunning(true);
        redraw();
        thread.setRunning(false);
    }
}
