package pac.man;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Paint;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.content.Context;
import android.widget.Toast;

import pac.man.util.Vector;
import pac.man.util.Animation;

import pac.man.model.GameState;
import pac.man.model.Character;
import pac.man.model.Character.AnimationType;
import pac.man.model.Level;
import pac.man.model.Player;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    Context context;
    MainThread thread;
    ResourceManager resMgr;
    boolean initalized = false;

    private int levelCounter = 0;
    Level[] levels;

    GameState gameState;
    Player player;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;

        resMgr = new ResourceManager(this, context);

        thread = new MainThread(getHolder(), this);
        thread.start();
    }

    private void init() {
        // Levels
        levels = new Level[2];
        levels[0] = resMgr.getLevel(R.raw.test_level);
        levels[1] = resMgr.getLevel(R.raw.test_level2);

        // Sounds
        resMgr.loadSound(R.raw.sound);

        // Animations
        Map<Character.AnimationType, Animation> animations
            = new EnumMap<Character.AnimationType, Animation>(Character.AnimationType.class);

        // FIXME Shit, this is ugly...
        ArrayList<Map<Character.AnimationType, Animation>> ghosts
            = new ArrayList<Map<Character.AnimationType, Animation>>();
        ghosts.add(new EnumMap<Character.AnimationType, Animation>(Character.AnimationType.class));
        ghosts.add(new EnumMap<Character.AnimationType, Animation>(Character.AnimationType.class));
        ghosts.add(new EnumMap<Character.AnimationType, Animation>(Character.AnimationType.class));
        ghosts.add(new EnumMap<Character.AnimationType, Animation>(Character.AnimationType.class));

        // Player animootions.
        animations.put(AnimationType.IDLE, resMgr.getAnimation(R.drawable.idle, 1, 10000));
        animations.put(AnimationType.RIGHT, resMgr.getAnimation(R.drawable.right, 4, 500));
        animations.put(AnimationType.UP, resMgr.getAnimation(R.drawable.up, 4, 500));
        animations.put(AnimationType.LEFT, resMgr.getAnimation(R.drawable.left, 4, 500));
        animations.put(AnimationType.DOWN, resMgr.getAnimation(R.drawable.down, 4, 500));
        animations.put(AnimationType.SPECIAL, resMgr.getAnimation(R.drawable.boing, 2, 500));
        animations.put(AnimationType.DEATH, resMgr.getAnimation(R.drawable.death, 13, 1300));

        // Ghost animutions
        ghosts.get(0).put(AnimationType.IDLE, resMgr.getAnimation(R.drawable.red_down, 2, 500));
        ghosts.get(0).put(AnimationType.RIGHT, resMgr.getAnimation(R.drawable.red_right, 2, 500));
        ghosts.get(0).put(AnimationType.UP, resMgr.getAnimation(R.drawable.red_up, 2, 500));
        ghosts.get(0).put(AnimationType.DOWN, resMgr.getAnimation(R.drawable.red_down, 2, 500));
        ghosts.get(0).put(AnimationType.LEFT, resMgr.getAnimation(R.drawable.red_left, 2, 500));
        ghosts.get(0).put(AnimationType.DEATH, resMgr.getAnimation(R.drawable.ill_white, 2, 500));
        ghosts.get(0).put(AnimationType.SPECIAL, resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

        ghosts.get(1).put(AnimationType.IDLE, resMgr.getAnimation(R.drawable.green_down, 2, 500));
        ghosts.get(1).put(AnimationType.RIGHT, resMgr.getAnimation(R.drawable.green_right, 2, 500));
        ghosts.get(1).put(AnimationType.UP, resMgr.getAnimation(R.drawable.green_up, 2, 500));
        ghosts.get(1).put(AnimationType.DOWN, resMgr.getAnimation(R.drawable.green_down, 2, 500));
        ghosts.get(1).put(AnimationType.LEFT, resMgr.getAnimation(R.drawable.green_left, 2, 500));
        ghosts.get(1).put(AnimationType.DEATH, resMgr.getAnimation(R.drawable.ill_white, 2, 500));
        ghosts.get(1).put(AnimationType.SPECIAL, resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

        ghosts.get(2).put(AnimationType.IDLE, resMgr.getAnimation(R.drawable.blue_down, 2, 500));
        ghosts.get(2).put(AnimationType.RIGHT, resMgr.getAnimation(R.drawable.blue_right, 2, 500));
        ghosts.get(2).put(AnimationType.UP, resMgr.getAnimation(R.drawable.blue_up, 2, 500));
        ghosts.get(2).put(AnimationType.DOWN, resMgr.getAnimation(R.drawable.blue_down, 2, 500));
        ghosts.get(2).put(AnimationType.LEFT, resMgr.getAnimation(R.drawable.blue_left, 2, 500));
        ghosts.get(2).put(AnimationType.DEATH, resMgr.getAnimation(R.drawable.ill_white, 2, 500));
        ghosts.get(2).put(AnimationType.SPECIAL, resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

        ghosts.get(3).put(AnimationType.IDLE, resMgr.getAnimation(R.drawable.orange_down, 2, 500));
        ghosts.get(3).put(AnimationType.RIGHT, resMgr.getAnimation(R.drawable.orange_right, 2, 500));
        ghosts.get(3).put(AnimationType.UP, resMgr.getAnimation(R.drawable.orange_up, 2, 500));
        ghosts.get(3).put(AnimationType.DOWN, resMgr.getAnimation(R.drawable.orange_down, 2, 500));
        ghosts.get(3).put(AnimationType.LEFT, resMgr.getAnimation(R.drawable.orange_left, 2, 500));
        ghosts.get(3).put(AnimationType.DEATH, resMgr.getAnimation(R.drawable.ill_white, 2, 500));
        ghosts.get(3).put(AnimationType.SPECIAL, resMgr.getAnimation(R.drawable.ill_blue, 2, 500));

        player = new Player(new Vector(-200,0), animations);
        gameState = new GameState(player, levels[levelCounter], ghosts);

        gameState.setNumOpponents(PacMan.pNOps);
        
        initalized = true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!initalized) {
            init();
            thread.setRunning(true);
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

        gameState.draw(canvas);

        int score = gameState.getScore();
        int lives = gameState.getLives();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(22);

        canvas.drawText(String.format("%d", score),
                        10, getHeight() - 20, paint);

        paint.setColor(Color.MAGENTA);
        canvas.drawText(String.format("%d", lives),
                        getWidth() - 20, getHeight() - 20, paint);
    }

    public void update(long dt, Canvas canvas) {
        gameState.update(dt, canvas);

        if(gameState.playerWon()) {
            levelCounter = (levelCounter + 1) % levels.length;
            PacMan.showMessage(String.format("Level %d", levelCounter + 1));

            gameState.setLevel(levels[levelCounter]);
        }
    }

    public void restartLevel() {
        thread.setRunning(false);

        PacMan.showMessage("Level restarted.");
        gameState.restartLevel();

        thread.setRunning(true);
        redraw();
        thread.setRunning(false);
    }
}