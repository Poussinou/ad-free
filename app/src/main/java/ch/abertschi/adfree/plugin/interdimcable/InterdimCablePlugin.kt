/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.interdimcable

import android.content.Context
import android.view.View
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.model.YamlRemoteConfigFactory
import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginContet
import ch.abertschi.adfree.view.AppSettings
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast

/**
 * Created by abertschi on 21.04.17.
 */
class InterdimCablePlugin : AdPlugin, AnkoLogger {

    private companion object {
        val BASE_URL: String = AppSettings.AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
        val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + AppSettings.GITHUB_RAW_SUFFIX
        val TITLE: String = "interdimensional cable"
        val ERROR_PLUGIN_SETTINGS: String = "Can not load interdimensional cable commercials. Did you check your internet?"
        val ERROR_NO_CHANNELS: String = "No channels to play. You can not listen to interdimensional tv :("
    }

    lateinit var configFactory: YamlRemoteConfigFactory<InterdimCableModel>

    private var audioPlayer: InterdimCablePlayer? = null
    private var model: InterdimCableModel? = null
    private var interdimCableView: InterdimCableView? = null

    override fun title(): String = TITLE
    override fun hasSettingsView(): Boolean = true

    override fun settingsView(context: Context): View? {
        if (interdimCableView == null) {
            interdimCableView = InterdimCableView(context)
        }
        return interdimCableView?.onCreate(this)
    }

    override fun onPluginActivated(context: PluginContet) {
        configFactory = YamlRemoteConfigFactory(PLUGIN_FILE_PATH, InterdimCableModel::class.java
                , PreferencesFactory.providePrefernecesFactory(context.applicationContext))

        model = configFactory.loadFromLocalStore()
        audioPlayer = InterdimCablePlayer(PreferencesFactory.providePrefernecesFactory(context.applicationContext))
        updatePluginSettings(context.applicationContext)
    }

    override fun onPluginDeactivated(context: PluginContet) {}

    override fun play(context: PluginContet) {
        if (model == null) {
            updatePluginSettings(context.applicationContext)
            return
        }
        val url: String? = getTrackUrl(model)
        if (url != null) {
            audioPlayer?.playAudio(url, context.applicationContext)
        } else {
            context.applicationContext.longToast(ERROR_NO_CHANNELS)
            error(ERROR_NO_CHANNELS)
        }
    }

    override fun playTrial(context: PluginContet) = play(context)

    fun configureAudioVolume(context: Context) {
        audioPlayer?.configureAudioVolume(context)
    }

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {
        if (audioPlayer?.isPlaying() == false) onStoped()
        else audioPlayer?.addCallbackForOnStop(onStoped)
    }

    override fun forceStop(context: PluginContet) {
        audioPlayer?.releasePlayer(context.applicationContext)
    }

    private fun getTrackUrl(model: InterdimCableModel?): String? {
        if (model?.channels?.size!! > 0) {
            return BASE_URL + model!!.channels!![(Math.random() * model!!.channels!!.size).toInt()].path + AppSettings.GITHUB_RAW_SUFFIX
        } else {
            return null
        }
    }

    private fun updatePluginSettings(context: Context) {
        configFactory.downloadObservable().subscribe(
                { pair ->
                    info("downloaded interdimensional cable plugin meta data for " + model?.channels?.size + " channels")

                    model = pair.first
                    configFactory.storeToLocalStore(model!!)
                },
                { error ->
                    context.applicationContext.longToast(ERROR_PLUGIN_SETTINGS)
                }
        )
    }
}