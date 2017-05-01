/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import ch.abertschi.adfree.plugin.AdPlugin
import ch.abertschi.adfree.plugin.PluginContet

/**
 * Created by abertschi on 01.05.17.
 */
class LocalMusicPlugin: AdPlugin {

    private var player: LocalMusicPlayer ? = null;

    override fun title(): String = "local music"

    override fun play(context: PluginContet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun playTrial(context: PluginContet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun forceStop(context: PluginContet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPluginActivated(context: PluginContet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPluginDeactivated(context: PluginContet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}