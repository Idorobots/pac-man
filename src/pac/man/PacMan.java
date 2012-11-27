package pac.man;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class PacMan extends Activity {

    private GamePanel gamePanel;

    // TODO: powiązanie parametrów menu z parametrami gry
    
    String nOpponentsPreference;
    String speedPreference;
    
    private void getPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        speedPreference = prefs.getString("speedPref", "Normal");
        nOpponentsPreference = prefs.getString("opponentsPref", "4");
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gamePanel = new GamePanel(this);
        setContentView(gamePanel);
        
        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        SoundManager.init(this);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        boolean retry = true;
//        while (retry) {
//            try {
//                gamePanel.getThread().join();
//                retry = false;
//            } catch (InterruptedException e) {
//            }
//        }
//    }

    @Override
    public void onStop() {
        gamePanel.getThread().setRunning(false);
        super.onStop();
    }
    
}
