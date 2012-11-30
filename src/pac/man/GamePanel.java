package pac.man;

import java.util.EnumMap;
import java.util.Map;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.content.Context;

import pac.man.util.Vector;
import pac.man.util.Animation;

import pac.man.model.Character;
import pac.man.model.Character.AnimationType;
import pac.man.model.Level;
import pac.man.model.Player;
import pac.man.model.Ghost;

import pac.man.ctrl.Strict4WayMovement;
import pac.man.ctrl.SimpleChaseStrategy;
import pac.man.ctrl.BouncyCollisions;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    MainThread thread;
    ResourceManager resMgr;

    Player player;
    Level level;
    Ghost ghost;
    long counter = 0;

    boolean initalized = false;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        resMgr = new ResourceManager(this, context);

        thread = new MainThread(getHolder(), this);
        thread.start();
    }

    private void init() {
        initalized = true;

        resMgr.loadSound(R.raw.sound);

        level = resMgr.getLevel(R.raw.test_level2);
        level.setCollisionHandler(new BouncyCollisions());

        Map<Character.AnimationType, Animation> animations
            = new EnumMap<Character.AnimationType, Animation>(Character.AnimationType.class);
        Map<Character.AnimationType, Animation> ghostAnimations
            = new EnumMap<Character.AnimationType, Animation>(Character.AnimationType.class);

        // Player animootions.
        animations.put(AnimationType.IDLE, resMgr.getAnimation(R.drawable.idle, 1, 1000));
        animations.put(AnimationType.RIGHT, resMgr.getAnimation(R.drawable.right, 4, 500));
        animations.put(AnimationType.UP, resMgr.getAnimation(R.drawable.up, 4, 500));
        animations.put(AnimationType.LEFT, resMgr.getAnimation(R.drawable.left, 4, 500));
        animations.put(AnimationType.DOWN, resMgr.getAnimation(R.drawable.down, 4, 500));
        animations.put(AnimationType.DEATH, resMgr.getAnimation(R.drawable.idle, 1, 1000));

        // Ghost animutions
        ghostAnimations.put(AnimationType.IDLE, resMgr.getAnimation(R.drawable.red_down, 2, 1000));
        ghostAnimations.put(AnimationType.RIGHT, resMgr.getAnimation(R.drawable.red_right, 2, 500));
        ghostAnimations.put(AnimationType.UP, resMgr.getAnimation(R.drawable.red_up, 2, 500));
        ghostAnimations.put(AnimationType.DOWN, resMgr.getAnimation(R.drawable.red_down, 2, 500));
        ghostAnimations.put(AnimationType.LEFT, resMgr.getAnimation(R.drawable.red_left, 2, 500));
        ghostAnimations.put(AnimationType.DEATH, resMgr.getAnimation(R.drawable.ill_white, 2, 500));
        ghostAnimations.put(AnimationType.SPECIAL, resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

        Rect g = level.randomEnemySpawn();
        Rect r = level.randomPlayerSpawn();

        player = new Player(new Vector(r.left, r.top), animations);
        player.setMovementAlgorithm(new Strict4WayMovement());

        ghost = new Ghost(new Vector(g.left, g.top), ghostAnimations);
        ghost.setMovementAlgorithm(new Strict4WayMovement());
        ghost.setMovementStrategy(new SimpleChaseStrategy(player, 100.0, 1.0));
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
            // resMgr.playSound(R.raw.sound);
        }

        return true;
    }

    public MainThread getThread() {
        return thread;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        level.draw(canvas);
        ghost.draw(canvas);
        player.draw(canvas);
    }

    public void update(long dt, Canvas canvas) {
        player.update(dt, canvas);

        if((counter % 15) == 0) {
            ghost.handleMove();
        }
        counter++;
        ghost.update(dt, canvas);

        level.update(dt, canvas, player);
        level.update(dt, canvas, ghost);
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