/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.localmusic

import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.widget.Toast

/**
 * Created by abertschi on 01.05.17.
 */
class LocalMusicView {

    private val FILE_SELECT_CODE: Int = 1

//    fun chooseFile() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.setType("*/*")      //all files
////        intent.setType("text/xml")   //XML file only
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//
//        try {
//            ActivityCompat.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
//
//        }
//    }

}