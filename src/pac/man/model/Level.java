package pac.man.model;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Rect;

import pac.man.util.Vector;
import pac.man.ctrl.CollisionHandler;
import pac.man.ctrl.StickyCollisions;

public class Level {
    public static final int WALL         = 0x000000;
    public static final int ENEMY_SPAWN  = 0xff0000;
    public static final int PLAYER_SPAWN = 0x00ff00;
    public static final int POWER_SPAWN  = 0x0000ff;
    public static final int GOLD_SPAWN   = 0xffffff;

    private int blockSize = 14;
    private int height = 0;
    private int width = 0;

    private ArrayList<Rect> blocks;
    private ArrayList<Rect> enemySpawns;
    private ArrayList<Rect> playerSpawns;

    private CollisionHandler collisionHandler;
    private Random random = new Random();

    public Level(Bitmap layout, int displayW, int displayH) {
        width = layout.getWidth();
        height = layout.getHeight();

        blockSize = (int) Math.min((double) displayW/(width-1), (double) displayH/(height-1));

        blocks = new ArrayList<Rect>();
        playerSpawns = new ArrayList<Rect>();
        enemySpawns = new ArrayList<Rect>();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                Rect r = new Rect(j*blockSize, i*blockSize,
                                  (j+1)*blockSize, (i+1)*blockSize);

                int pixel = layout.getPixel(j, i) & 0x00ffffff;

                switch(pixel) {
                    case WALL:         blocks.add(r); break;
                    case ENEMY_SPAWN:  enemySpawns.add(r); break;
                    case PLAYER_SPAWN: playerSpawns.add(r); break;
                    case POWER_SPAWN:  break; // TODO
                    case GOLD_SPAWN:   break; // TODO

                    default: System.out.println(String.format("Bad level data: %x", pixel));
                }
            }
        }

        // Defaults to sticky collisions.
        collisionHandler = new StickyCollisions();
    }

    public void update(long dt, Canvas canvas, Character c) {
        if(!c.isMoving()) return; // No need to do anything.

        Rect p = c.getBoundingRect();

        for(Rect b : blocks) {
            if(Rect.intersects(p, b)) {
                collisionHandler.handle(dt, canvas, b, c);
                c.setActiveAnimation();
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

        // NOTE Just for debugging purposes.
        // TODO Remove

        p.setColor(Color.GREEN);
        for(Rect r : playerSpawns) {
            canvas.drawRect(r, p);
        }

        p.setColor(Color.RED);
        for(Rect r : enemySpawns) {
            canvas.drawRect(r, p);
        }

    }

    public Rect randomPlayerSpawn() {
        int s = playerSpawns.size();

        if(s != 0) return playerSpawns.get(random.nextInt(s));
        else       return null;
    }

    public Rect randomEnemySpawn() {
        int s = enemySpawns.size();

        if(s != 0) return enemySpawns.get(random.nextInt(s));
        else       return null;
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public void setCollisionHandler(CollisionHandler c) {
        collisionHandler = c;
    }
}