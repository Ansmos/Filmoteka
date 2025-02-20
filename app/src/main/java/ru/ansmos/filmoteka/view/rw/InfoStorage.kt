package ru.ansmos.filmoteka.view.rw


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.paging.PagedListAdapter
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.filmoteka.R


class InfoStorage {
    fun getData(startPosition: Int, loadSize: Int): MutableList<Info> {
        val endPos = minOf(startPosition + loadSize - 1, 100)
        return (startPosition..endPos).map { Info(it + 1, "Info num ${it + 1}") } as MutableList<Info>
    }

}

data class Info(val id: Int, val name: String) {
    override fun toString() = "$id - $name"
}


class InfoDiffer() {
    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Info>(){
            override fun areItemsTheSame(oldItem: Info, newItem: Info): Boolean = oldItem?.id == newItem?.id
            override fun areContentsTheSame(oldItem: Info, newItem: Info) = oldItem == newItem
        }
    }

}


class InfoDataSource(private val storage: InfoStorage): PositionalDataSource<Info>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Info>) {
        Log.d("INSPECT", "loadInitial, requestedStartPosition = ${params.requestedStartPosition}, requestedLoadSize = ${params.requestedLoadSize}")
        val result = storage.getData(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(result, params.requestedStartPosition)
    }
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Info>) {
        Log.d("INSPECT", "loadRange, startPosition = ${params.startPosition}, loadSize = ${params.loadSize}")
        val result = storage.getData(params.startPosition, params.loadSize)
        callback.onResult(result)
    }
}

class InfoAdapter(itemDiffer: DiffUtil.ItemCallback<Info>) : PagedListAdapter<Info, InfoAdapter.InfoVH>(itemDiffer) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return InfoVH(view)
    }

    override fun onBindViewHolder(holder: InfoVH, position: Int) {
        currentList?.get(position)?.also { holder.bind(it) }
    }

    class InfoVH(itemView: View) :  RecyclerView.ViewHolder(itemView) {
        fun bind(item: Info) {
            (itemView as TextView).text = item.toString()
        }
    }
}

