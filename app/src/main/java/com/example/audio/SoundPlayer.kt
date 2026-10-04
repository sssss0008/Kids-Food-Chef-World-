package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.ToneGenerator
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundPlayer(private val context: Context) {

  private var toneGenerator: ToneGenerator? = null
  private val scope = CoroutineScope(Dispatchers.Default)

  init {
    try {
      toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
    } catch (_: Exception) {
    }
  }

  fun playClick(enabled: Boolean = true) {
    if (!enabled) return
    try {
      toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
    } catch (_: Exception) {}
  }

  fun playSuccess(enabled: Boolean = true) {
    if (!enabled) return
    scope.launch {
      playToneSequence(listOf(523, 659, 784, 1046), durationMs = 80)
    }
  }

  fun playStar(enabled: Boolean = true) {
    if (!enabled) return
    scope.launch {
      playToneSequence(listOf(784, 988, 1175), durationMs = 70)
    }
  }

  fun playCookSizzle(enabled: Boolean = true) {
    if (!enabled) return
    scope.launch {
      playToneSequence(listOf(350, 420, 390, 480), durationMs = 90)
    }
  }

  fun playBlend(enabled: Boolean = true) {
    if (!enabled) return
    scope.launch {
      playToneSequence(listOf(220, 330, 440, 550, 660), durationMs = 60)
    }
  }

  private fun playToneSequence(frequencies: List<Int>, durationMs: Int) {
    val sampleRate = 44100
    try {
      frequencies.forEach { freq ->
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
          val angle = 2.0 * Math.PI * i / (sampleRate / freq.toDouble())
          val envelope = 1.0 - (i.toDouble() / numSamples)
          buffer[i] = (sin(angle) * 16384 * envelope).toInt().toShort()
        }

        val track = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_GAME)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(buffer.size * 2)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        track.write(buffer, 0, buffer.size)
        track.play()
        Thread.sleep(durationMs.toLong())
        track.release()
      }
    } catch (_: Exception) {}
  }

  fun triggerHaptic(enabled: Boolean = true) {
    if (!enabled) return
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
      }
    } catch (_: Exception) {}
  }
}
