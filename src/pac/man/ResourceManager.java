package pac.man;

import java.util.Map;
import java.util.HashMap;

import android.graphics.BitmapFactory;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import pac.man.util.Animation;
import pac.man.model.Level;

public class ResourceManager {
    private Context context;
    private View view;

    private SoundPool soundPool;
    private Map<Integer, Integer> sounds = new HashMap<Integer, Integer>();
    private Map<Integer, Boolean> soundLoaded = new HashMap<Integer, Boolean>();

    public ResourceManager(View v, Context c) {
        view = v;
        context = c;

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
          @Override
          public void onLoadComplete(SoundPool soundPool, int sampleID, int status) {
              soundLoaded.put(sampleID, true);
          }
        });
    }

    public Level getLevel(int id) {
        return new Level(BitmapFactory.decodeResource(view.getResources(), id),
                         view.getWidth(), view.getHeight());
    }

    public Animation getAnimation(int id, int frames, int duration) {
        return new Animation(BitmapFactory.decodeResource(view.getResources(), id),
                             frames, duration);
    }

    public void loadSound(int id) {
        sounds.put(id, soundPool.load(context, id, 1));
    }

    public void playSound(int id) {
        // Getting the user sound settings
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        if(sounds.containsKey(id)) {
            int sound = sounds.get(id);
            boolean loaded = soundLoaded.get(sound);

            if(loaded) {
                soundPool.play(sound, volume, volume, 1, 0, 1f);
            }
        }
    }
}