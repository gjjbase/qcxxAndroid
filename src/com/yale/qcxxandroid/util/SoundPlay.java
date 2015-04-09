package com.yale.qcxxandroid.util;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;



/***
public static SoundPlay soundPlay;
soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
		soundPlay.loadSfx(context, R.raw.up, ID_SOUND_UP);
		soundPlay.play(ID_SOUND_UP, 0);
*****/



public class SoundPlay {

	int streamVolume;

	private SoundPool soundPool;

	private HashMap<Integer, Integer> soundPoolMap;

	/***************************************************************
	 * Function: initSounds(); Parameters: null Returns: None. Description:
	 * ��ʼ������ϵͳ Notes: none.
	 ***************************************************************/
	public void initSounds(Context context) {
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);

		soundPoolMap = new HashMap<Integer, Integer>();

		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	/**
	 * Notes: none.
	 */
	public void loadSfx(Context context, int raw, int ID) {
		soundPoolMap.put(ID, soundPool.load(context, raw, 1));
	}

	/***************************************************************
	 ***************************************************************/
	public void play(int sound, int uLoop) {
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				uLoop, 1f);
	}
}
