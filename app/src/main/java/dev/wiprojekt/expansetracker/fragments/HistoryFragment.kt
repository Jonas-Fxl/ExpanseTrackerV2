package dev.wiprojekt.expansetracker.fragments


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import dev.wiprojekt.expansetracker.R

class HistoryFragment : Fragment() {

    //Store uris der Fotos
    private var images: ArrayList<Uri>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_history, container, false)
    }

}
