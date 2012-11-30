package pac.man;

import java.util.EnumMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundManager {

    public static enum Sound {
        SAMPLE1
    }

    private static Context context;

    // Dźwięki.
    private static SoundPool soundPool;
    private static Map<Sound, Integer> sounds = new EnumMap<Sound, Integer>(Sound.class);
    private static boolean loaded = false;

    private SoundManager() {
        context = null;
    }

    public static void init(Context _context) {
        context = _context;
        loadAll();
    }

    @SuppressLint("NewApi")
    private static void loadAll() {
        // Load the sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
          @Override
          public void onLoadComplete(SoundPool soundPool, int sampleId,
              int status) {
            loaded = true;
          }
        });
        sounds.put(Sound.SAMPLE1, soundPool.load(context, R.raw.sound, 1));
    }

    public static void play(Sound sound) {
        // Getting the user sound settings
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (loaded) {
            soundPool.play(sounds.get(sound), volume, volume, 1, 0, 1f);
        }
    }
}
