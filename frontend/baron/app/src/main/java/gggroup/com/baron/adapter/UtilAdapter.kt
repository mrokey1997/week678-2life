package gggroup.com.baron.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.chip.Chip
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import gggroup.com.baron.R
import kotlinx.android.synthetic.main.item_utils.view.*

class UtilAdapter(private val utils: ArrayList<String>, val context: Context?) : RecyclerView.Adapter<UtilAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chip: Chip? = view.chip_util
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_utils, parent, false)
        return MyViewHolder(itemView)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when(utils[position]){
            "Máy lạnh" -> {
                holder.chip?.setChipTextResource(R.string.air_conditioner)
                holder.chip?.setChipIconResource(R.drawable.utils_air_conditioner)
            }
            "Máy giặt" -> {
                holder.chip?.setChipTextResource(R.string.washing)
                holder.chip?.setChipIconResource(R.drawable.utils_washing_machine)
            }
            "Tủ lạnh" -> {
                holder.chip?.setChipTextResource(R.string.fridge)
                holder.chip?.setChipIconResource(R.drawable.utils_fridge)
            }
            "WC riêng" -> {
                holder.chip?.setChipTextResource(R.string.toilet)
                holder.chip?.setChipIconResource(R.drawable.utils_toilet)
            }
            "Chổ để xe" -> {
                holder.chip?.setChipTextResource(R.string.bike_parking)
                holder.chip?.setChipIconResource(R.drawable.utils_bike_parking)
            }
            "Wifi" -> {
                holder.chip?.setChipTextResource(R.string.wifi)
                holder.chip?.setChipIconResource(R.drawable.utils_wifi)
            }
            "Giờ giấc tự do" -> {
                holder.chip?.setChipTextResource(R.string.freedom)
                holder.chip?.setChipIconResource(R.drawable.utils_freedom)
            }
            "Không chung chủ" -> {
                holder.chip?.setChipTextResource(R.string.house_key)
                holder.chip?.setChipIconResource(R.drawable.utils_house_key)
            }
            "Bếp" -> {
                holder.chip?.setChipTextResource(R.string.kitchen)
                holder.chip?.setChipIconResource(R.drawable.utils_kitchen)
            }
            "Giường ngủ" -> {
                holder.chip?.setChipTextResource(R.string.bed)
                holder.chip?.setChipIconResource(R.drawable.utils_bed)
            }
            "Tivi" -> {
                holder.chip?.setChipTextResource(R.string.television)
                holder.chip?.setChipIconResource(R.drawable.utils_television)
            }
            "Tủ quần áo" -> {
                holder.chip?.setChipTextResource(R.string.closet)
                holder.chip?.setChipIconResource(R.drawable.utils_closet)
            }
            "Gác lửng" -> {
                holder.chip?.setChipTextResource(R.string.mezzanine)
                holder.chip?.setChipIconResource(R.drawable.utils_mezzanine)
            }
            "Camera" -> {
                holder.chip?.setChipTextResource(R.string.camera)
                holder.chip?.setChipIconResource(R.drawable.utils_camera)
            }
            "Bảo vệ" -> {
                holder.chip?.setChipTextResource(R.string.security)
                holder.chip?.setChipIconResource(R.drawable.utils_security_man)
            }
            "Thú cưng" -> {
                holder.chip?.setChipTextResource(R.string.pet)
                holder.chip?.setChipIconResource(R.drawable.utils_pet)
            }
        }
    }

    override fun getItemCount(): Int {
        return utils.size
    }
}