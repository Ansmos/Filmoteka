import java.text.SimpleDateFormat
import java.util.concurrent.Executors

//Executors.newSingleThreadExecutor().execute {
    val w = System.currentTimeMillis()
    Thread.sleep(1000)
    val w1 = System.currentTimeMillis()
    println(w)
    println(SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(w))
    println(w1)
    println(SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(w1))
//}
Вопрос 1
fun clearAllFilms() : Int {
    var deletedItemsCount : Int = 0
    Executors.newSingleThreadExecutor().execute {
        deletedItemsCount = filmDao.clearAll()
    }
    //Омновной поток не ждет другого, поэтому возвращает 0, если через дебаг, правильно. Как сделать возврат?
    return deletedItemsCount
}

fun convertliveEntityToFilms(list: LiveData<List<FilmEntity>>): LiveData<List<Film>> {

    val tt = Transformations.map(list){ filmEntityList ->
        val rrr = arrayListOf<Film>()
        filmEntityList.forEach {
            convertEntityToFilm(it)?.let { it1 -> rrr.add(it1) }
        }
        return@map rrr.toList()
    }
    return tt


    val source = MutableLiveData<FilmEntity>()
    source.postValue(FilmEntity(id=1, id_tmdb = 1, title = "title", poster = "poster", description = "desc", release_date = "01", rating = 0.0))
}
