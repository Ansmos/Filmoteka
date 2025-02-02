import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine
//import kotlinx.coroutines.*

//Executors.newSingleThreadExecutor().execute {
    val w = System.currentTimeMillis()
    Thread.sleep(1000)
    val w1 = System.currentTimeMillis()
    println(w)
    println(SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(w))
    println(w1)
    println(SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(w1))
//}




val f = flow{
    repeat(10){
        emit(it)
    }
}




