package pac.man.model;

import java.util.Map;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Rect;

import pac.man.ctrl.MovementAlgorithm;
import pac.man.ctrl.Strict4WayMovement;
import pac.man.ctrl.InertialMovement;
import pac.man.ctrl.NonrestrictiveMovement;
import pac.man.ctrl.MovementStrategy;
import pac.man.ctrl.SimpleChaseStrategy;
import pac.man.ctrl.RandomStrategy;
import pac.man.ctrl.CollisionHandler;
import pac.man.ctrl.StickyCollisions;
import pac.man.ctrl.BouncyCollisions;
import pac.man.model.Level;
import pac.man.model.Level.CollisionCallback;
import pac.man.model.Character;
import pac.man.util.Animation;
import pac.man.util.Vector;

public class GameState {
    public static final int STARTING_LIVES = 3;
    public static final int GOLD_VALUE = 10;
    public static final int POWER_VALUE = 25;
    public static final int GHOST_VALUE = 100;
    public static final int POWERUP_DURATION = 3000;
    public static final int POWERUP_THRESHOLD = 750;
    public static final int GHOST_MOVE_INTERVAL = 175;

    private boolean running = true;
    private int numOpponents = 4;
    private int lives;
    private int score;

    private Level level;
    private Player player;
    private Ghost[] ghosts;

    private long ghostMovementCounter = 0;
    private long modeCounter = 0;
    private long thresholdCounter = 0;
    private boolean normalMode = true;

    public GameState(Player player, Level level, ArrayList<Map<Character.AnimationType, Animation>> ghosts) {
        assert player != null;
        assert level != null;
        assert ghosts.size() >= 1;

        this.level = level;
        this.player = player;

        int size = ghosts.size();
        this.ghosts = new Ghost[size];

        for(int i = 0; i < size; ++i) {
            this.ghosts[i] = new Ghost(new Vector(-100, 0), ghosts.get(i));
        }

        restartLevel();
    }

    private void handleInteractions() {
        // Player - Ghost collisions:

        Rect p = player.getBoundingRect();
        boolean special = player.isSpecial();

        int len = Math.min(ghosts.length, numOpponents);

        for(int i = 0; i < len; ++i) {
            if(!ghosts[i].isAlive()) continue;

            Rect g = ghosts[i].getBoundingRect();

            if(Rect.intersects(p, g)) {
                if(special) {
                    ghosts[i].setAlive(false);
                    ghosts[i].setSpeed(new Vector(0, 0));
                    ghosts[i].setMovementStrategy(new RandomStrategy());
                    ghosts[i].setMovementAlgorithm(new Strict4WayMovement(0.5)); // Slow the ghost down.
                    score += GHOST_VALUE;
                }
                else {
                    lives--;

                    if(lives <= 0) {
                        player.setAlive(false);
                        player.setSpeed(new Vector(0, 0));
                        running = false;
                    }
                    else {
                        // TODO Message indication.
                        resetLevel();
                    }
                }

                break;
            }
        }

        if(level.getTotalGold() == 0) {
            running = false;
            return;
        }

        // End game logic:
        for(int i = 0; i < len; ++i) {
            if(ghosts[i].isAlive()) return;
        }

        running = false;
    }

    public void update(long dt, Canvas canvas) {
        modeCounter -= dt;

        if(!normalMode && modeCounter <= 0) {
             setNormalMode();
        }

        if(running) {
            handleInteractions();

            player.update(dt, canvas);
            level.update(dt, canvas, player);

            int len = Math.min(numOpponents, ghosts.length);
            for(int i = 0; i < len; ++i) {
                if((ghostMovementCounter % GHOST_MOVE_INTERVAL) == 0) {
                    ghosts[i].handleMove();
                }

                ghosts[i].update(dt, canvas);
                level.update(dt, canvas, ghosts[i]);
            }

            ghostMovementCounter += dt;
        }
        else {
            player.update(dt, canvas);
        }
    }

    public void draw(Canvas canvas) {
        level.draw(canvas);

        int len = Math.min(numOpponents, ghosts.length);

        if(normalMode || (modeCounter > POWERUP_THRESHOLD) || (thresholdCounter % 6 < 3)) {
            for(int i = 0; i < len; ++i) {
                ghosts[i].draw(canvas);
            }
        }
        thresholdCounter++;

        player.draw(canvas);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean playerWon() {
        return !running && (lives > 0);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level l) {
        this.level = l;
        restartLevel();
    }

    private void setNormalMode() {
        normalMode = true;

        player.setMovementAlgorithm(new Strict4WayMovement());
        player.setSpecial(false);

        for(Ghost ghost : ghosts) {
            if(!ghost.isAlive()) continue;

            ghost.setMovementAlgorithm(new Strict4WayMovement());
            ghost.setMovementStrategy(new SimpleChaseStrategy(player, 150.0, 1.0));
            ghost.setSpecial(false);
        }

        level.setCollisionHandler(new StickyCollisions());
    }

    private void setPowerupMode() {
        normalMode = false;

        //player.setMovementAlgorithm(new NonrestrictiveMovement());
        player.setMovementAlgorithm(new InertialMovement(player.getSpeed(), 23.0));
        player.setSpecial(true);

        for(Ghost ghost : ghosts) {
            ghost.setMovementAlgorithm(new NonrestrictiveMovement());
            ghost.setMovementStrategy(new SimpleChaseStrategy(player, 150.0, -1.0));
            ghost.setSpecial(true);
        }

        level.setCollisionHandler(new BouncyCollisions());

        modeCounter = POWERUP_DURATION;
    }

    public void restartLevel() {
        resetLevel();

        running = true;
        normalMode = true;
        lives = STARTING_LIVES;

        player.setSpeed(new Vector(0, 0));

        level.setCollisionCallback(new CollisionCallback() {
            public boolean onWall(Character who) {
                return false;
            }
            public boolean onPowerup(Character who) {
                if(who == player) {
                    setPowerupMode();
                    score += POWER_VALUE;
                    return true;
                }
                return false;
            }
            public boolean onGold(Character who) {
                if(who == player) {
                    score += GOLD_VALUE;
                    return true;
                }
                return false;
            }
        });
    }

    public void resetLevel() {
        assert level != null;
        assert player != null;
        assert ghosts != null;

        player.setPosition(level.randomPlayerSpawn());
        player.setAlive(true);
        player.setSpecial(false);

        for(Ghost ghost : ghosts) {
            ghost.setPosition(level.randomEnemySpawn());
            ghost.setAlive(true);
            ghost.setSpecial(false);
        }

        setNormalMode();
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumOpponents() {
       return numOpponents;
    }

    public void setNumOpponents(int numOpponents) {
        this.numOpponents = numOpponents;
    }

    public long getPowerupTime() {
        if(normalMode) return 0;
        else           return modeCounter;
    }
}