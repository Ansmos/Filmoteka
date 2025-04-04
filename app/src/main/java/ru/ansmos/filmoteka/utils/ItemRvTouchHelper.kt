package ru.ansmos.filmoteka.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.view.rw.NotificationAdapter

class ItemRvTouchHelper(val adapter: NotificationAdapter) : ItemTouchHelper.Callback() {

    val interactor = App.instance.dagger.getInteractor()
    override fun isLongPressDragEnabled(): Boolean = false //Не поддерживается

    override fun isItemViewSwipeEnabled(): Boolean = true // Поддерживается

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //Удаляем элемент из списка после жеста swipe
        interactor.cancelNotification(
            adapter.removeNotification(viewHolder.bindingAdapterPosition))
    }
}