package com.minovepole.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.minovepole.R

object SoundManager {
    private lateinit var soundPool: SoundPool
    private var winSoundId: Int = 0
    private var explodeSoundId: Int = 0
    private var isLoaded = false

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

    fun playWinSound() {
        if (isLoaded) {
            soundPool.play(winSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun playExplosionSound() {
        if (isLoaded) {
            soundPool.play(explodeSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        if (isLoaded) {
            soundPool.release()
            isLoaded = false
        }
    }
}