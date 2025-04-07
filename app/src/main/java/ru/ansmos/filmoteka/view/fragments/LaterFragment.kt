package ru.ansmos.filmoteka.view.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.filmoteka.App
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.bll.Notification
import ru.ansmos.filmoteka.databinding.FragmentLaterBinding
import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator
import ru.ansmos.filmoteka.utils.*
import ru.ansmos.filmoteka.view.MainActivity
import ru.ansmos.filmoteka.view.rw.*
import ru.ansmos.filmoteka.viewmodel.LaterFragmentViewModel

class LaterFragment : Fragment() {
    private lateinit var binding : FragmentLaterBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(LaterFragmentViewModel::class.java)
    }
    private lateinit var notificationsAdapter: NotificationAdapter
    private lateinit var layoutManagerRV: LinearLayoutManager
    private val autoDisposable = AutoDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaterBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AnimationHelper.performFragmentCircularRevealAnimation(requireActivity().findViewById(R.id.later_fragment_root), requireActivity(), 3)

        viewModel.notificationsRx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                notificationsAdapter.clearItems()
                notificationsAdapter.addItems(it)
            },{
                println(it.message)
            }).addTo(autoDisposable)
        initSeelaterRV()
    }

    private fun initSeelaterRV() {
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        binding.laterRv.apply {
            notificationsAdapter =
                NotificationAdapter(object : NotificationAdapter.IOnItemClicListener {
                    override fun click(notif: Notification, isEditNotification: Boolean) {
                        if (isEditNotification){
                            // меняем нотификацию
                            val notification = viewModel.getNotification(notif.id)
                            if (notification != null) {
                                notification
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({
                                        App.instance.dagger.getNotificationHelper()
                                            .notificationSet(requireContext(), it)
                                        Log.i("seeLaterFragment", "!!! Notification")
                                    },{
                                        println(it.message)
                                    })
                            }
                        } else {
                            val film = viewModel.getFilm(notif.filmId)
                            film.observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    (requireActivity() as MainActivity).launchDetailsFragment(it)
                                    Log.i("seeLaterFragment", "!!! Film")
                                })
                        }
                    }
                })
            //Присваиваем адаптер
            adapter = notificationsAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())
            layoutManagerRV = layoutManager as LinearLayoutManager
            //Применяем декоратор для отступов
            val decorator = FilmsRVItemDecorator(10)
            addItemDecoration(decorator)
            //Удаление через смахивание
            val callback = ItemRvTouchHelper(notificationsAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)

        }
    }
}

