package gggroup.com.baron.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.esafirm.imagepicker.model.Image
import gggroup.com.baron.R
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class ImageAdapter(private val images: ArrayList<Image>, val context: Context?) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {
    val array: ArrayList<Boolean> = arrayListOf(false,false,false,false,false,false,false,false,false,false)
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView)
        var overlay: View = view.findViewById(R.id.overlay)
        var delete: Button = view.findViewById(R.id.bt_delete)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val opacity = 100 // from 0 to 255
        holder.overlay.setBackgroundColor(opacity * 0x1000000) // black with a variable alpha
        @Suppress("DEPRECATION")
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT)
        params.gravity = Gravity.BOTTOM
        holder.overlay.layoutParams = params
        holder.overlay.invalidate() // update the view
        //val myBitmap = BitmapFactory.decodeFile(images[position].path)
        //BlurImage.with(context).load(myBitmap).intensity(20F).Async(true).into(holder.image)
        val nbThreads = Thread.getAllStackTraces().keys.size
        var mExecutor = Executors.newFixedThreadPool(nbThreads)
        if(position<2) {
            mExecutor = Executors.newFixedThreadPool(nbThreads - 1)
        }
        else if (position < 4){
            mExecutor = Executors.newFixedThreadPool(nbThreads - 2 )
        }
//        else if (position < 8){
//            mExecutor = Executors.newFixedThreadPool(nbThreads - 3 )
//        }
        mExecutor.execute {
            holder.image.setImageBitmap(
                    BitmapFactory.decodeFile(images[position].path)
            )
        }

        mExecutor.shutdown()
        mExecutor.awaitTermination(java.lang.Long.MAX_VALUE, TimeUnit.DAYS)

        //after all threads finish
       // holder.image.setImageBitmap(BitmapFactory.decodeFile(images[position].path))
        holder.image.setOnClickListener {
            if (array[position]) {
                holder.overlay.visibility = View.INVISIBLE
                holder.delete.visibility = View.INVISIBLE
                array[position] = !array[position]
            } else {
                holder.overlay.visibility = View.VISIBLE
                holder.delete.visibility = View.VISIBLE
                array[position] = !array[position]
            }
        }
        holder.delete.setOnClickListener {
            images.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }

        //holder.image.setImageURI(Uri.fromFile(File(images[position].path)))

    }
    override fun getItemCount(): Int {
        return images.size
    }
}