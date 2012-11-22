package pac.man.model;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class Level {
    public static final int BLOCK_SIZE = 14;

    public static final int WALL         = 0x000000;
    public static final int ENEMY_SPAWN  = 0xff0000;
    public static final int PLAYER_SPAWN = 0x00ff00;
    public static final int POWER_SPAWN  = 0x0000ff;
    public static final int GOLD_SPAWN   = 0xffffff;

    private int height = 0;
    private int width = 0;

    private ArrayList<Rect> blocks;
    private ArrayList<Rect> enemySpawns;
    private ArrayList<Rect> playerSpawns;

    private Random random = new Random();

    public Level(Bitmap layout) {
        width = layout.getWidth();
        height = layout.getHeight();

        blocks = new ArrayList<Rect>();
        playerSpawns = new ArrayList<Rect>();
        enemySpawns = new ArrayList<Rect>();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                Rect r = new Rect(j*BLOCK_SIZE, i*BLOCK_SIZE,
                                    (j+1)*BLOCK_SIZE, (i+1)*BLOCK_SIZE);

                int pixel = layout.getPixel(j, i) & 0x00ffffff;
                System.out.println(String.format("%d, %d = %x", j, i, pixel));

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
    }

    public void update(long dt, Character c) {
       // TODO Handle player-wall collisions.
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);

        for(Rect r : blocks) {
            canvas.drawRect(r, p);
        }

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
}