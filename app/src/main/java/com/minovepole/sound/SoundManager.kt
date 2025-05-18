package com.minovepole.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.minovepole.R

/**
 * Singleton object that manages sounds, and is in charge of playing them
 *
 * Uses the SoundPool API to play sounds
 *
 * Has to be initialised before use, and then released on app close
 */
object SoundManager {
    private lateinit var soundPool: SoundPool
    private var winSoundId: Int = 0
    private var explodeSoundId: Int = 0
    private var isLoaded = false

    /**
     * Initializes the SoundPool and loads sound resources.
     *
     * @param context Application context used to load raw sound resources.
     */
    fun init(context: Context) {
        if (isLoaded) return
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        winSoundId = soundPool.load(context, R.raw.win, 1)
        explodeSoundId = soundPool.load(context, R.raw.explosion, 1)
        isLoaded = true
    }

    /**
     * Plays the win sound
     */
    fun playWinSound() {
        if (isLoaded) {
            soundPool.play(winSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    /**
     * Plays the explosion sound
     */
    fun playExplosionSound() {
        if (isLoaded) {
            soundPool.play(explodeSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    /**
     * Releases resources on app close
     */
    fun release() {
        if (isLoaded) {
            soundPool.release()
            isLoaded = false
        }
    }
}