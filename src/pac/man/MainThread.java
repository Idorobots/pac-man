package pac.man;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final int TIME_DELTA = 10; //ms

    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        Canvas canvas;
        long t0 = System.currentTimeMillis();
        long accumulator = 0;

        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();

                synchronized (surfaceHolder) {
                    long dt = System.currentTimeMillis() - t0;

                    t0 += dt;
                    accumulator += dt;

                    while(accumulator > TIME_DELTA) {
                        accumulator -= TIME_DELTA;
                        gamePanel.update(TIME_DELTA, canvas);
                    }
                    gamePanel.draw(canvas);
                }

            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}