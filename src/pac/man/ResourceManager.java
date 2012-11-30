package pac.man;

import android.graphics.BitmapFactory;

import pac.man.GamePanel;
import pac.man.gfx.Animation;
import pac.man.model.Level;

public class ResourceManager {
    private GamePanel panel;

    public ResourceManager(GamePanel gp) {
        panel = gp;
    }

    public Level getLevel(int id) {
        return new Level(BitmapFactory.decodeResource(panel.getResources(), id),
                         panel.getWidth(), panel.getHeight());
    }

    public Animation getAnimation(int id, int frames, int duration) {
        return new Animation(BitmapFactory.decodeResource(panel.getResources(), id),
                             frames, duration);
    }
}