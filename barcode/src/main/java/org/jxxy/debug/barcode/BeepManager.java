package org.jxxy.debug.barcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.RawRes;

import java.io.Closeable;
import java.io.IOException;

/** @author asus */
public final class BeepManager implements MediaPlayer.OnErrorListener, Closeable {

    private static final String TAG = BeepManager.class.getSimpleName();
    private static final String BEEP_TAG = "ifBeep";
    private static final String VIBRATE_TAG = "ifVibrate";
    @SuppressLint("StaticFieldLeak")
    public static BeepManager instance;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static String SP_TAG = "";
    private MediaPlayer mediaPlayer;
    /** 音量 */
    private float beepVolume;
    /** 震动时间 */
    private long vibrateDuration;
    /** 音频文件 */
    @RawRes private int beepRes;
    private boolean shouldPlayBeep;
    private boolean shouldVibrate;

    private BeepManager() {
        this.mediaPlayer = null;
    }

    public static BeepManager getInstance() {
        if (instance == null) {
            synchronized (BeepManager.class) {
                if (instance == null) {
                    instance = new BeepManager();
                }
            }
        }
        return instance;
    }

    public static void init(Context context, String sp) {
        mContext = context;
        SP_TAG = sp;
    }

    public BeepManager setBeepVolume(float beepVolume) throws CustomException {
        if (instance == null) {
            throw new CustomException("尚未初始化 BeepManager");
        }
        this.beepVolume = beepVolume;
        return this;
    }

    public BeepManager setVibrateDuration(long vibrateDuration)
            throws CustomException {
        if (instance == null) {
            throw new CustomException("尚未初始化 BeepManager");
        }
        this.vibrateDuration = vibrateDuration;
        return this;
    }

    public BeepManager setBeepRes(@RawRes int beepRes) throws CustomException {
        if (instance == null) {
            throw new CustomException("尚未初始化 BeepManager");
        }
        this.beepRes = beepRes;
        return this;
    }

    public void build() {
        initParam();
    }

    private void initParam() {
        SharedPreferences prefs = mContext.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
        shouldPlayBeep = prefs.getBoolean(BEEP_TAG, true);
        shouldVibrate = prefs.getBoolean(VIBRATE_TAG, true);
        shouldPlayBeep = shouldBeep();
        beepVolume = 0.10f;
        vibrateDuration = 200L;
        beepRes = R.raw.beep;
    }

    private synchronized void updatePrefs(Activity activity) {
        if (shouldPlayBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
            // so we now play on the music stream.
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = buildMediaPlayer(activity);
        }
    }

    public synchronized void playBeepSoundAndVibrate(Activity activity) {
        updatePrefs(activity);
        if (shouldBeep() && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (shouldVibrate) {
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(vibrateDuration);
        }
    }

    private boolean shouldBeep() {
        if (shouldPlayBeep) {
            // See if sound settings overrides this
            AudioManager audioService =
                    (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                shouldPlayBeep = false;
            }
        }
        return shouldPlayBeep;
    }

    private MediaPlayer buildMediaPlayer(Context activity) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try (AssetFileDescriptor file = activity.getResources().openRawResourceFd(beepRes)) {
            mediaPlayer.setDataSource(
                    file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(beepVolume, beepVolume);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer.release();
            return null;
        }
    }

    @Override
    public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
        close();
        return true;
    }

    @Override
    public synchronized void close() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
