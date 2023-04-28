package com.translator.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.translator.R
import com.translator.model.data.DataModel

class MainAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var playArticulationClickListener: OnPlayArticulationClickListener
) : RecyclerView.Adapter<MainAdapter.RecyclerItemViewHolder>() {

    private var data: List<DataModel> = listOf()
    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_recyclerview_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    findViewById<TextView>(R.id.header_textview_recycler_item).text = data.text

                    findViewById<TextView>(R.id.description_textview_recycler_item).text =
                        data.meanings?.get(0)?.translation?.translation

                    findViewById<TextView>(R.id.transcription_textview_recycler_item).text =
                        "[" + data.meanings?.get(0)?.transcription + "]"

                    setOnClickListener { openInNewWindow(data) }
                    findViewById<AppCompatImageButton>(R.id.play_articulation).setOnClickListener {
                        data.meanings?.get(0)?.soundUrl?.let { sound_url ->
                            playArticulationClickListener.onPlayClick(
                                sound_url
                            )
                        }
                    }
                }
            }
        }
    }

    private fun openInNewWindow(listItemData: DataModel) {
        onListItemClickListener.onItemClick(listItemData)
    }


    interface OnListItemClickListener {
        fun onItemClick(data: DataModel)
    }

    interface OnPlayArticulationClickListener {
        fun onPlayClick(url: String)
    }
}
