/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.interdimcable

import android.content.Context
import ch.abertschi.adfree.model.PreferencesFactory
import ch.abertschi.adfree.plugin.AbstractMusicPlayer
import org.jetbrains.anko.longToast
import org.jetbrains.anko.runOnUiThread

/**
 * Created by abertschi on 01.05.17.
 */
class InterdimCablePlayer(prefs: PreferencesFactory) : AbstractMusicPlayer(prefs) {

    override fun onAudioLoading(context: Context) {
        context.runOnUiThread {
            longToast("Downloading interdimensional cable ads ...")
        }
    }
}