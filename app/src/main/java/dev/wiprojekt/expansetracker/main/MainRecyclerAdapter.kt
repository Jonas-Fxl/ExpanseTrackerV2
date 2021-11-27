package dev.wiprojekt.expansetracker.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.data.Buchung


class MainRecyclerAdapter(val context: Context, val data : List<Buchung>) : RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>(){

    inner class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val nameText = itemView.findViewById<TextView>(R.id.nameText)
        val viewImage = itemView.findViewById<ImageView>(R.id.viewImage)
        val viewPreis = itemView.findViewById<TextView>(R.id.viewPreis)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = this.data[position]
        with(holder){
            nameText?.let { it.text = data.bezeichnung
                            it.contentDescription = data.bezeichnung
            }
            viewPreis?.text = data.summe.toString()
        }
    }

    override fun getItemCount() = data.size
    }
