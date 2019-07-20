package cn.vailing.chunqiu.promethues.util;

import android.content.Context;
import android.util.Log;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;

import java.io.IOException;

/**
 * Created by dream on 2017/9/24.
 */

public class SoundHelper {
    private static SoundHelper soundHelper = new SoundHelper();
    private Context context;
    private Engine engine;
    private Sound mFireSound;
    private Sound mflySound;
    private Sound mboomSound;
    private Sound mdeviceDownSound;
    private Sound mdeviceUpSound;
    private Sound mAccelerateSound;
    private Sound lighting;

    private SoundHelper() {
    }

    public void init(Engine engine, Context context) {
        this.engine = engine;
        this.context = context;
    }

    public static SoundHelper getInstance() {
        return soundHelper;
    }

    public void initFlame() {
        try {
            mFireSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/flameFire.ogg");
            mflySound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/flameFly.ogg");
            mboomSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/flameBoom.ogg");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initLighting(){
        try {
            lighting = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/lighting.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initAccelerate() {
        try {
            mdeviceDownSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceDown.ogg");
            mdeviceUpSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceUp.ogg");
            mAccelerateSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/accelerate.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Sound getLighting() {
        return lighting;
    }

    public void stop() {
        if (mFireSound != null)
            mFireSound.stop();
        if (mflySound != null)
            mflySound.stop();
        if (mboomSound != null)
            mboomSound.stop();
        if (mdeviceDownSound != null)
            mdeviceDownSound.stop();
        if (mdeviceUpSound != null)
            mdeviceUpSound.stop();
        if (mAccelerateSound != null)
            mAccelerateSound.stop();
        if (lighting != null)
            lighting.stop();
    }

    public Sound getMboomSound() {
        return mboomSound;
    }

    public Sound getMflySound() {
        return mflySound;
    }

    public Sound getmFireSound() {
        return mFireSound;
    }
}
