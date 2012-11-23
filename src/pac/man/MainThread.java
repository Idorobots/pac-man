package pac.man;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

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
        final int sleepInterval = 10;

        Canvas canvas;
        long t0 = System.currentTimeMillis();
        long accumulator = 0;

        while (true) {
            // Prevent other threads' starvation.
            try {
                synchronized (this) {
                    while (!running)
                        wait();
                }
            } catch (InterruptedException e) {
            }

            System.out.println("running");

            // FIXME: w przypadku wtrzymania wątku trzeba reagować natychmiast,
            // a nie dopiero po paru(nastu) iteracjach związanych z akumulatorem
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();

                synchronized (surfaceHolder) {
                    long dt = System.currentTimeMillis() - t0;

                    t0 += dt;
                    accumulator += dt;

                    while (accumulator > GamePanel.TIME_DELTA) {
                        accumulator -= GamePanel.TIME_DELTA;
                        gamePanel.update(GamePanel.TIME_DELTA, canvas);
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

    synchronized public void setRunning(boolean running) {
        this.running = running;

        if (running)
            notify();
    }
}