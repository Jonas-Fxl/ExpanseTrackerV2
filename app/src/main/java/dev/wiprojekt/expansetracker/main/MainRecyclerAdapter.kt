package dev.wiprojekt.expansetracker.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.preferences.PrefHelper
import java.text.SimpleDateFormat
import java.util.*


class MainRecyclerAdapter(val context: Context, val data: List<Buchung>, val itemListener: BuchungItemListener) :
    RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemName = itemView.findViewById<TextView>(R.id.item_name)

        // val viewImage = itemView.findViewById<ImageView>(R.id.viewImage)
        val itemPrice = itemView.findViewById<TextView>(R.id.item_price)
        val itemCategory = itemView.findViewById<TextView>(R.id.item_category)
        val itemDate = itemView.findViewById<TextView>(R.id.item_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pref = PrefHelper
        pref.loadSettings(context)
        val data = this.data[position]
        with(holder) {
            if (data.summe < 0) {
                itemPrice?.text = "%.2f ${PrefHelper.currency}".format(data.summe).replace("-", "- ")
            } else {
                itemPrice?.text = "+ %.2f ${PrefHelper.currency}".format(data.summe)
                itemPrice?.setTextColor(Color.parseColor("#34AA44"))
            }
            itemName?.text = data.bezeichnung
            itemCategory?.text = data.art

            itemDate?.text = convertLongToTime(data.datum)

            holder.itemView.setOnClickListener{
                itemListener.onBuchungItemClick(data)
            }
        }
    }

    override fun getItemCount() = data.size

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd.MM.yyyy")
        return df.parse(date).time
    }

    interface BuchungItemListener{
        fun onBuchungItemClick(buchung: Buchung)
    }
}
