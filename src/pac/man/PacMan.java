package pac.man;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class PacMan extends Activity {
	
    boolean isPaused = false;

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
            Toast.makeText(PacMan.this, "Pause is Selected", Toast.LENGTH_SHORT).show();
            isPaused = true;
            return true;

        case R.id.menu_resume:
            Toast.makeText(PacMan.this, "Resume is Selected", Toast.LENGTH_SHORT).show();
            isPaused = false;
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isPaused) {
            menu.findItem(R.id.menu_resume).setVisible(true);
            menu.findItem(R.id.menu_pause).setVisible(false);
        } else {
            menu.findItem(R.id.menu_resume).setVisible(false);
            menu.findItem(R.id.menu_pause).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);

    }	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GamePanel(this));
        
        // XXX: debug
//        setContentView(R.layout.main);
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        System.exit(0);
//    }
}
