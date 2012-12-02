package pac.man;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import pac.man.ctrl.MovementAlgorithm;
import pac.man.ctrl.MovementAlgorithm.Speed;
import pac.man.model.GameState;
import pac.man.model.Player;
import pac.man.util.Vector;

public class PacMan extends Activity implements SensorEventListener {

    private static PacMan instance; 
    private GamePanel gamePanel = null;

    // Accelerometer
    private float mLastX, mLastY;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    
    public static Speed pSpeed = null;
    public static int pNOps = -1;

    private void getPrefs() {
        // TODO: powiązanie parametrów menu z parametrami gry
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String speedPreference = prefs.getString("speedPref", "Normal");
        String nOpponentsPreference = prefs.getString("opponentsPref", "4");

        for (Speed s : Speed.values()) {
            if (s.getLabel().equals(speedPreference)) {
                pSpeed = s;
                MovementAlgorithm.setSpeed(s);
                System.out.println(s.getGain());
            }
        }
        
        pNOps = Integer.parseInt(nOpponentsPreference);

        if (gamePanel != null && gamePanel.player != null) {
            Player p = gamePanel.player;
            Vector speed = new Vector(p.getSpeed().normalize().scale(MovementAlgorithm.getSpeed().getGain()));
            p.setSpeed(speed);
        }

        if (gamePanel != null && gamePanel.gameState != null) {
            // FIXME Doesn't work.
            GameState g = gamePanel.gameState;
            g.setNumOpponents(Integer.parseInt(nOpponentsPreference));
            System.out.println("Num Opponents: " + nOpponentsPreference);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPrefs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        menu.findItem(R.id.menu_resume).setVisible(false);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected Identify single menu
     * item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.menu_settings:
            Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
            System.out.println("settings");
            startActivity(settingsActivity);
            return true;

        case R.id.menu_pause:
            gamePanel.getThread().setRunning(false);
            Toast.makeText(PacMan.this, "Game paused", Toast.LENGTH_SHORT).show();
            return true;

        case R.id.menu_resume:
            gamePanel.getThread().setRunning(true);
            Toast.makeText(PacMan.this, "Game resumed", Toast.LENGTH_SHORT).show();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (gamePanel.getThread().getRunning()) {
            menu.findItem(R.id.menu_resume).setVisible(false);
            menu.findItem(R.id.menu_pause).setVisible(true);
        } else {
            menu.findItem(R.id.menu_resume).setVisible(true);
            menu.findItem(R.id.menu_pause).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);

    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gamePanel = new GamePanel(this);
        setContentView(gamePanel);

        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    public static PacMan getInstance() {
        return instance;
    }

    // @Override
    // protected void onDestroy() {
    // super.onDestroy();
    //
    // boolean retry = true;
    // while (retry) {
    // try {
    // gamePanel.getThread().join();
    // retry = false;
    // } catch (InterruptedException e) {
    // }
    // }
    // }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onStop() {
        gamePanel.getThread().setRunning(false);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final double RESTART_ACCELERATION_THRESHOLD = 8.0;

        float x = event.values[0];
        float y = event.values[1];

        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);

            if (deltaX > RESTART_ACCELERATION_THRESHOLD || deltaY > RESTART_ACCELERATION_THRESHOLD) {
                gamePanel.restartLevel();
//                gamePanel.getThread().setRunning(!gamePanel.getThread().getRunning());
            }

            mLastX = x;
            mLastY = y;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            if (gamePanel.getThread().is)
            gamePanel.getThread().setRunning(false);
        }

        return super.onKeyUp(keyCode, event);
    }
    
    public static void showMessage(final String str) {
        // FIXME Crashes the game.
        getInstance().runOnUiThread(new Runnable() {
            public void run() {
              Toast.makeText(getInstance(), str, Toast.LENGTH_SHORT).show();
            }
          });
        
//         Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }    
}
