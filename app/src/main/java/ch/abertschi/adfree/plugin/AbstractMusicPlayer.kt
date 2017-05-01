/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.view.AppSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

/**
 * Created by abertschi on 01.05.17.
 *
 * Lifecycle:
 * Caller calls #playAudio() or #getMediaPlayerObservable
 * - #onAudioLoading()
 * - #onAudioPlaying()
 * - #onPlayerClose()
 */
abstract class AbstractMusicPlayer(val prefs: PreferencesFactory) : AnkoLogger {

    private var player: MediaPlayer? = null
    private var isPlaying: Boolean = false

    open fun isPlaying(): Boolean = isPlaying

    open fun playAudio(url: String, context: Context) {
        runAndCatchException(context, {
            val proxy = AppSettings.instance(context).getHttpProxy()
            val proxyUrl = proxy.getProxyUrl(url)
            getMediaPlayerObservable(context, proxyUrl).subscribe { player ->
                this.player = player
                player.setOnErrorListener { _, what, _ -> throw RuntimeException("Problem with audio player, code: " + what) }
                player.start()
                onAudioPlaying(context)
                isPlaying = true
            }
        })
    }

    open fun onAudioLoading(context: Context) {
        context.runOnUiThread {
            longToast("Loading music ...")
        }
    }

    open fun onPlayerClose(context: Context) {}

    open fun onAudioPlaying(context: Context) {}

    open fun onAudioPlayerException(e: Throwable, context: Context) {
        context.applicationContext.runOnUiThread {
            longToast("Whooops, there was an error with audio")
            error(e)
        }
    }

    open fun getMediaPlayerObservable(context: Context, url: String): Observable<MediaPlayer>
            = Observable.create<MediaPlayer> { source ->
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.mode = AudioManager.MODE_RINGTONE
        player = MediaPlayer()
        player?.setDataSource(url)
        player?.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)

        var asyncPreparationDone = false
        player?.prepareAsync()
        Observable.just(true)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (!asyncPreparationDone) {
                onAudioLoading(context)
            }
        }
        player?.setOnPreparedListener {
            asyncPreparationDone = true
            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, prefs.loadAudioVolume(context), AudioManager.FLAG_SHOW_UI)
            player?.setOnCompletionListener {
                releasePlayer(context)
            }
            source.onNext(player)
        }
    }

    open fun configureAudioVolume(context: Context) {
        val am = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, prefs.loadAudioVolume(context), AudioManager.FLAG_SHOW_UI)
        Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {

            val volume = am.getStreamVolume(AudioManager.STREAM_VOICE_CALL)
            info("Storing audio volume with value " + volume)
            prefs.storeAudioVolume(volume, context)
        }
    }

    private fun runAndCatchException(context: Context, function: () -> Unit): Unit {
        try {
            function()
        } catch(e: Throwable) {
            onAudioPlayerException(e, context)
        }
    }

    open fun releasePlayer(context: Context) {
        runAndCatchException(context, {
            onPlayerClose(context)
            isPlaying = false
            player?.stop()
            player?.release()
            player = null
        })
    }
}