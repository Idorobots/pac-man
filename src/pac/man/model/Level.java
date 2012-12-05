package pac.man.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pac.man.ctrl.CollisionHandler;
import pac.man.ctrl.StickyCollisions;
import pac.man.util.Animation;
import pac.man.util.Vector;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


// TODO Random level generator.
public class Level {
    public static final long AWFUL_HAX   = 23;

    public static final int WALL         = 0x000000;
    public static final int ENEMY_SPAWN  = 0xff0000;
    public static final int PLAYER_SPAWN = 0x00ff00;
    public static final int POWER_SPAWN  = 0x0000ff;
    public static final int GOLD_SPAWN   = 0xffff00;

    public interface CollisionCallback {
        public boolean onWall(Character who);
        public boolean onGold(Character who);
        public boolean onPowerup(Character who);
    }

    private int blockSize = 14;
    private int height = 0;
    private int width = 0;

    private List<Rect> blocks;
    private List<Rect> enemySpawns;
    private List<Rect> playerSpawns;
    private List<Rect> powerSpawns;
    private List<Rect> goldSpawns;

    private Animation gold;
    private Animation powerup;

    private CollisionHandler collisionHandler;
    private CollisionCallback collisionCallback = null;

    private Random random = new Random();

    Bitmap layout;
    int displayW;
    int displayH;

    public Level(Animation gold, Animation powerup, Bitmap layout, int displayW, int displayH) {
        // TODO Rect merging.

        this.powerup = powerup;
        this.gold = gold;

        this.layout = layout;
        this.displayW = displayW;
        this.displayH = displayH;

        width = layout.getWidth();
        height = layout.getHeight();

        blockSize = (int) Math.min((double) displayW/(width-1), (double) displayH/(height-1));

        blocks = Collections.synchronizedList(new ArrayList<Rect>());
        playerSpawns = Collections.synchronizedList(new ArrayList<Rect>());
        enemySpawns = Collections.synchronizedList(new ArrayList<Rect>());
        powerSpawns = Collections.synchronizedList(new ArrayList<Rect>());
        goldSpawns = Collections.synchronizedList(new ArrayList<Rect>());

        init();

        // Defaults to sticky collisions.
        collisionHandler = new StickyCollisions();
    }

    public synchronized void init() {
        blocks.clear();
        playerSpawns.clear();
        enemySpawns.clear();
        powerSpawns.clear();
        goldSpawns.clear();

        int goldW = gold.getWidth();
        int goldH = gold.getHeight();

        int powerW = powerup.getWidth();
        int powerH = powerup.getHeight();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                Rect r = new Rect(j*blockSize, i*blockSize,
                                  (j+1)*blockSize, (i+1)*blockSize);

                int pixel = layout.getPixel(j, i) & 0x00ffffff;

                switch(pixel) {
                    case WALL:
                        blocks.add(r);
                    break;
                    case ENEMY_SPAWN:
                        enemySpawns.add(r);
                    break;
                    case PLAYER_SPAWN:
                        playerSpawns.add(r);
                    break;
                    case POWER_SPAWN:
                        powerSpawns.add(new Rect(r.left, r.top,
                                                 r.left + powerW, r.top + powerH));
                    break;
                    case GOLD_SPAWN:
                        goldSpawns.add(new Rect(r.left, r.top,
                                                r.left + goldH, r.top + goldH));
                    break;

                    default: break;
                }
            }
        }
    }

    public synchronized void update(long dt, Canvas canvas, Character c) {
        // Update animations:
        powerup.update(dt);
        gold.update(dt);

        if(!c.isMoving()) return; // No need to do anything.

        Rect p = c.getBoundingRect();

        Iterator<Rect> blockIter = blocks.iterator();
        while (blockIter.hasNext()) {
            Rect b = blockIter.next();
            if(Rect.intersects(p, b)) {
                collisionHandler.handle(dt, canvas, b, c);

                if(collisionCallback != null) {
                    if(collisionCallback.onWall(c)) {
                        blockIter.remove();
                    }
                }
                break;
            }
        }

        Iterator<Rect> powerIter = powerSpawns.iterator();
        while (powerIter.hasNext()) {
            Rect b = powerIter.next();
            if(Rect.intersects(p, b)) {
                if(collisionCallback != null) {
                    if(collisionCallback.onPowerup(c)) {
                        powerIter.remove();
                    }
                }
                break;
            }
        }

        Iterator<Rect> goldIter = goldSpawns.iterator();
        while (goldIter.hasNext()) {
            if(Rect.intersects(p, goldIter.next())) {
                if(collisionCallback != null) {
                    if(collisionCallback.onGold(c)) {
                        goldIter.remove();
                    }
                }
                break;
            }
        }
    }

    public synchronized void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);

        for(Rect r : blocks) {
            canvas.drawRect(r, p);
        }

        for(Rect r : powerSpawns) {
            powerup.draw(r, canvas);
            powerup.update(AWFUL_HAX); // Fake independant animations.
        }

        for(Rect r : goldSpawns) {
            gold.draw(r, canvas);
        }
    }

    public Vector randomPlayerSpawn() {
        int s = playerSpawns.size();
        Rect r;

        if(s != 0) r = playerSpawns.get(random.nextInt(s));
        else       return new Vector(0, 0);

        return new Vector(r.left, r.top);
    }

    public Vector randomEnemySpawn() {
        int s = enemySpawns.size();
        Rect r;

        if(s != 0) r = enemySpawns.get(random.nextInt(s));
        else       return new Vector(0, 0);

        return new Vector(r.left, r.top);
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public void setCollisionHandler(CollisionHandler c) {
        collisionHandler = c;
    }

    public void setCollisionCallback(CollisionCallback cc) {
        collisionCallback = cc;
    }

    public int getTotalGold() {
        int totalGold = goldSpawns.size();
        int totalPower = powerSpawns.size();
        return totalGold + totalPower;
    }
}