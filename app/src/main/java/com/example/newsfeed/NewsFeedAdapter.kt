package com.example.newsfeed

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.newsfeed.R.id.image
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class NewsFeedAdapter( private val listener: NewsItemCLicked): RecyclerView.Adapter<NewsViewHolder>() {
    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return  items.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.authorView.text = currentItem.author
        holder.sourceTextView.text = currentItem.source
        holder.publishedTimeView.text = currentItem.publishedAt
        Glide.with(holder.itemView).load(currentItem.imageUrl).listener(
            object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageReloadView.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageReloadView.visibility = View.GONE
                    return false
                }

            }
        ).into(holder.imageView)
    }

    fun updateNews(updatedItems: ArrayList<News>){
        items.clear()
        items.addAll(updatedItems)

        notifyDataSetChanged()
    }


}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView : TextView = itemView.findViewById(R.id.title_text)
    val imageView : ImageView = itemView.findViewById(image)
    val authorView : TextView = itemView.findViewById(R.id.author_name_text)
    val publishedTimeView: TextView = itemView.findViewById(R.id.publishedTime)
    val sourceTextView: TextView = itemView.findViewById(R.id.source)
    val imageReloadView: View = itemView.findViewById(R.id.reloadContainer)

}

interface NewsItemCLicked{
    fun onItemClicked(item: News)
}