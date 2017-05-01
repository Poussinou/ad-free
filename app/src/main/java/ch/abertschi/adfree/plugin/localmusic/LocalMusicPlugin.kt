/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.os.Environment
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginContet
import org.jetbrains.anko.longToast
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import java.io.File
import java.util.*


/**
 * Created by abertschi on 01.05.17.
 */
class LocalMusicPlugin : AdPlugin {

    private var player: LocalMusicPlayer? = null

    override fun title(): String = "local music"

    override fun play(context: PluginContet) {
        val url: File? = getUrl(context)
        if (url == null) noTracksFound(context)
        else {
            println("found url: " + url)
            showTrackTitle(context, "")
            player?.playAudio(url.toURL().toExternalForm(), context.applicationContext, cacheUrl = false)
        }
    }

    private fun showTrackTitle(context: PluginContet, title: String) {
        context.applicationContext.runOnUiThread {
            toast("Playing local music")
        }
    }

    private fun noTracksFound(context: PluginContet) {
        context.applicationContext.runOnUiThread {
            longToast("No audio tracks found to play")
        }
    }

    private fun getUrl(context: PluginContet): File? {
        val fileExt = listOf<String>(".mp3")
        val musicDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val allFiles = ArrayList<File>()
        val dirs = LinkedList<File>()
        dirs.add(musicDir)
        while (!dirs.isEmpty()) {
            val listFiles = dirs.poll().listFiles() ?: continue
            for (f in listFiles) {
                if (f.isDirectory()) {
                    dirs.add(f)
                } else if (f.isFile()) {
                    for (ext: String in fileExt) {
                        if (f.absoluteFile.toString().endsWith(ext)) {
                            println(f.absoluteFile)
                            allFiles.add(f)
                        }
                    }
                }
            }
        }
        return if (allFiles.size == 0) null else allFiles[(Math.random() * allFiles.size).toInt()]

    }

    override fun playTrial(context: PluginContet) = play(context)

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {
//        if (player?.isPlaying() == true) player?.addCallbackForOnStop(onStoped)
//        else { // TODO: Add option to end playing the current song?
            forceStop(contet)
            onStoped()
//        }
    }

    override fun forceStop(context: PluginContet) {
        player?.stopPlayer(context = context.applicationContext)
    }

    override fun onPluginActivated(context: PluginContet) {
        player = LocalMusicPlayer(PreferencesFactory.providePrefernecesFactory(context = context.applicationContext))

    }

    override fun onPluginDeactivated(context: PluginContet) {
        player?.stopPlayer(context = context.applicationContext)
    }

}