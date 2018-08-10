package gggroup.com.baron.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import gggroup.com.baron.R
import gggroup.com.baron.entities.Item

class ArrayAdapterWithIcon(val context: Activity, val list: ArrayList<Item>) :
        ArrayAdapter<Item>(context, R.layout.item_dialog, list) {

    internal class ViewHolder {
        var name: TextView? = null
        var flag: ImageView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            val inflater = context.layoutInflater
            view = inflater.inflate(R.layout.item_dialog, null)
            val viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.txt)
            viewHolder.flag = view.findViewById(R.id.img) as ImageView
            view.tag = viewHolder
        } else {
            view = convertView
        }

        val holder = view?.tag as ViewHolder
        holder.name?.text = list[position].text
        holder.flag?.setImageResource(list[position].icon!!)
        return view
    }
}