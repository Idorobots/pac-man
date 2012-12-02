package pac.man.model;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Rect;

import pac.man.util.Animation;
import pac.man.util.Vector;
import pac.man.ctrl.CollisionHandler;
import pac.man.ctrl.StickyCollisions;


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

    private ArrayList<Rect> blocks;
    private ArrayList<Rect> enemySpawns;
    private ArrayList<Rect> playerSpawns;
    private ArrayList<Rect> powerSpawns;
    private ArrayList<Rect> goldSpawns;

    private Animation gold;
    private Animation powerup;

    private CollisionHandler collisionHandler;
    private CollisionCallback collisionCallback = null;

    private Random random = new Random();

    public Level(Animation gold, Animation powerup, Bitmap layout, int displayW, int displayH) {
        // TODO Rect merging.

        this.powerup = powerup;
        this.gold = gold;

        width = layout.getWidth();
        height = layout.getHeight();

        blockSize = (int) Math.min((double) displayW/(width-1), (double) displayH/(height-1));

        blocks = new ArrayList<Rect>();
        playerSpawns = new ArrayList<Rect>();
        enemySpawns = new ArrayList<Rect>();
        powerSpawns = new ArrayList<Rect>();
        goldSpawns = new ArrayList<Rect>();

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

        // Defaults to sticky collisions.
        collisionHandler = new StickyCollisions();
    }

    public void update(long dt, Canvas canvas, Character c) {
        // Update animations:
        powerup.update(dt);
        gold.update(dt);

        if(!c.isMoving()) return; // No need to do anything.

        Rect p = c.getBoundingRect();

        for(Rect b : blocks) {
            if(Rect.intersects(p, b)) {
                collisionHandler.handle(dt, canvas, b, c);

                if(collisionCallback != null) {
                    if(collisionCallback.onWall(c)) {
                        blocks.remove(b);
                    }
                }
                break;
            }
        }

        for(Rect b : powerSpawns) {
            if(Rect.intersects(p, b)) {
                if(collisionCallback != null) {
                    if(collisionCallback.onPowerup(c)) {
                        powerSpawns.remove(b);
                    }
                }
                break;
            }
        }

        for(Rect b : goldSpawns) {
            if(Rect.intersects(p, b)) {
                if(collisionCallback != null) {
                    if(collisionCallback.onGold(c)) {
                        goldSpawns.remove(b);
                    }
                }
                break;
            }
        }
    }

    public void draw(Canvas canvas) {
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