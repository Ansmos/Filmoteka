package ru.ansmos.filmoteka.rw

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.db.Film

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class FilmAdapter(private val clickListener: IOnItemClixkListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val  items = mutableListOf<Film>()      //Здесь у нас хранится список элементов для RV

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent,false))
    }

    //В этом методе будет привязка полей из объекта Film к View из film_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Проверяем какой у нас ViewHolder
        when (holder){
            is FilmViewHolder -> {
                //Вызываем метод bind(), который мы создали, и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.bund(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                holder.itemView.findViewById<CardView>(R.id.item_container).setOnClickListener{
                    clickListener.click(items[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //Метод для добавления объектов в наш список
    fun addItems(list: List<Film>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    //Интерфейс для обработки кликов
    interface IOnItemClixkListener{
        fun click(film: Film)
    }

}