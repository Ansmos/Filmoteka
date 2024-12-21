[1mdiff --git a/app/build.gradle b/app/build.gradle[m
[1mindex ab64333..1ea54e3 100644[m
[1m--- a/app/build.gradle[m
[1m+++ b/app/build.gradle[m
[36m@@ -42,6 +42,10 @@[m [mdependencies {[m
     implementation 'androidx.recyclerview:recyclerview:1.2.1'[m
     implementation 'androidx.coordinatorlayout:coordinatorlayout:1.2.0'[m
 [m
[32m+[m[32m    //Glide[m
[32m+[m[32m    implementation 'com.github.bumptech.glide:glide:4.11.0'[m
[32m+[m[32m    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'[m
[32m+[m
     testImplementation 'junit:junit:4.13.2'[m
     androidTestImplementation 'androidx.test.ext:junit:1.1.5'[m
     androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'[m
[1mdiff --git a/app/src/main/java/ru/ansmos/filmoteka/DetailsFragment.kt b/app/src/main/java/ru/ansmos/filmoteka/DetailsFragment.kt[m
[1mindex 1405470..c6e8be4 100644[m
[1m--- a/app/src/main/java/ru/ansmos/filmoteka/DetailsFragment.kt[m
[1m+++ b/app/src/main/java/ru/ansmos/filmoteka/DetailsFragment.kt[m
[36m@@ -2,6 +2,7 @@[m [mpackage ru.ansmos.filmoteka[m
 [m
 import android.content.Intent[m
 import android.os.Bundle[m
[32m+[m[32mimport android.view.Gravity[m
 import androidx.fragment.app.Fragment[m
 import android.view.LayoutInflater[m
 import android.view.View[m
[36m@@ -13,12 +14,19 @@[m [mimport androidx.appcompat.widget.Toolbar[m
 import androidx.coordinatorlayout.widget.CoordinatorLayout[m
 import androidx.core.content.ContextCompat[m
 import androidx.core.view.get[m
[32m+[m[32mimport androidx.transition.Fade[m
[32m+[m[32mimport androidx.transition.Slide[m
 import com.google.android.material.floatingactionbutton.FloatingActionButton[m
 import com.google.android.material.snackbar.Snackbar[m
 import ru.ansmos.filmoteka.db.Film[m
 [m
 class DetailsFragment : Fragment() {[m
     lateinit var film: Film[m
[32m+[m
[32m+[m[32m    init {[m
[32m+[m[32m        enterTransition = Slide(Gravity.END).apply { duration = 500 }[m
[32m+[m[32m        returnTransition = Fade() //Slide(Gravity.END).apply { duration = 500; mode = Slide.MODE_OUT }[m
[32m+[m[32m    }[m
     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {[m
         return inflater.inflate(R.layout.fragment_details, container, false)[m
     }[m
[1mdiff --git a/app/src/main/java/ru/ansmos/filmoteka/HomeFragment.kt b/app/src/main/java/ru/ansmos/filmoteka/HomeFragment.kt[m
[1mindex 8694482..47eaa52 100644[m
[1m--- a/app/src/main/java/ru/ansmos/filmoteka/HomeFragment.kt[m
[1m+++ b/app/src/main/java/ru/ansmos/filmoteka/HomeFragment.kt[m
[36m@@ -2,6 +2,7 @@[m [mpackage ru.ansmos.filmoteka[m
 [m
 import android.content.Intent[m
 import android.os.Bundle[m
[32m+[m[32mimport android.view.Gravity[m
 import androidx.fragment.app.Fragment[m
 import android.view.LayoutInflater[m
 import android.view.View[m
[36m@@ -11,6 +12,7 @@[m [mimport androidx.appcompat.app.AppCompatDelegate[m
 import androidx.appcompat.widget.SearchView[m
 import androidx.recyclerview.widget.LinearLayoutManager[m
 import androidx.recyclerview.widget.RecyclerView[m
[32m+[m[32mimport androidx.transition.*[m
 import com.google.android.material.appbar.MaterialToolbar[m
 import ru.ansmos.filmoteka.db.Film[m
 import ru.ansmos.filmoteka.decor.FilmsRVItemDecorator[m
[36m@@ -21,18 +23,45 @@[m [mclass HomeFragment : Fragment() {[m
 [m
     private lateinit var filmsAdapter: FilmAdapter[m
 [m
[31m-    override fun onCreateView([m
[31m-        inflater: LayoutInflater,[m
[31m-        container: ViewGroup?,[m
[31m-        savedInstanceState: Bundle?[m
[31m-    ): View? {[m
[32m+[m[32m    init {[m
[32m+[m[32m        exitTransition = Fade() // Slide(Gravity.START).apply { duration = 500; mode = Slide.MODE_OUT }[m
[32m+[m[32m        reenterTransition = Slide(Gravity.START).apply { duration = 500; }[m
[32m+[m[32m    }[m
[32m+[m[32m    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {[m
         return inflater.inflate(R.layout.fragment_home, container, false)[m
     }[m
 [m
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {[m
         super.onViewCreated(view, savedInstanceState)[m
[31m-        initRV()[m
[32m+[m[32m        initAnimationEnter()[m
         initSearchView()[m
[32m+[m[32m        initRV()[m
[32m+[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private fun initAnimationEnter() {[m
[32m+[m[32m        val scene = Scene.getSceneForLayout(requireActivity().findViewById(R.id.home_fragment_root),[m
[32m+[m[32m            R.layout.merge_home_screen_content, requireContext())[m
[32m+[m[32m        //Создаем анимацию выезда поля поиска сверху[m
[32m+[m[32m        val searchSlide = Slide(Gravity.TOP).addTarget(R.id.search_view)[m
[32m+[m[32m        //Создаем анимацию выезда RV снизу[m
[32m+[m[32m        val recyclerSlide = Slide(Gravity.BOTTOM).addTarget(R.id.main_recycler)[m
[32m+[m[32m        //Создаем экземпляр TransitionSet, который объединит все наши анимации[m
[32m+[m[32m        val customTransition = TransitionSet().apply {[m
[32m+[m[32m            //Устанавливаем время, за которое будет проходить анимация[m
[32m+[m[32m            duration = 500[m
[32m+[m[32m            //Добавляем сами анимации[m
[32m+[m[32m            addTransition(recyclerSlide)[m
[32m+[m[32m            addTransition(searchSlide)[m
[32m+[m[32m        }[m
[32m+[m[32m        //Также запускаем через TransitionManager, но вторым параметром передаем нашу кастомную анимацию[m
[32m+[m[32m        //если это первый запуск[m
[32m+[m[32m        if ((requireActivity() as MainActivity).firstStart) {[m
[32m+[m[32m            TransitionManager.go(scene, customTransition)[m
[32m+[m[32m            (requireActivity() as MainActivity).firstStart = false[m
[32m+[m[32m        } else{[m
[32m+[m[32m            TransitionManager.go(scene)[m
[32m+[m[32m        }[m
     }[m
 [m
     private fun initSearchView() {[m
[1mdiff --git a/app/src/main/java/ru/ansmos/filmoteka/MainActivity.kt b/app/src/main/java/ru/ansmos/filmoteka/MainActivity.kt[m
[1mindex 5ada080..986583f 100644[m
[1m--- a/app/src/main/java/ru/ansmos/filmoteka/MainActivity.kt[m
[1m+++ b/app/src/main/java/ru/ansmos/filmoteka/MainActivity.kt[m
[36m@@ -19,6 +19,7 @@[m [mclass MainActivity : AppCompatActivity() {[m
     var darkMode = AppCompatDelegate.getDefaultNightMode()[m
     private var backPressed = 0L[m
     lateinit var filmsDataBase : List<Film>[m
[32m+[m[32m    var firstStart: Boolean = true[m
 [m
     override fun onCreate(savedInstanceState: Bundle?) {[m
         super.onCreate(savedInstanceState)[m
[1mdiff --git a/app/src/main/java/ru/ansmos/filmoteka/rw/FilmViewHolder.kt b/app/src/main/java/ru/ansmos/filmoteka/rw/FilmViewHolder.kt[m
[1mindex eb7fbc6..f0d89ed 100644[m
[1m--- a/app/src/main/java/ru/ansmos/filmoteka/rw/FilmViewHolder.kt[m
[1m+++ b/app/src/main/java/ru/ansmos/filmoteka/rw/FilmViewHolder.kt[m
[36m@@ -4,6 +4,7 @@[m [mimport android.view.View[m
 import android.widget.ImageView[m
 import android.widget.TextView[m
 import androidx.recyclerview.widget.RecyclerView[m
[32m+[m[32mimport com.bumptech.glide.Glide[m
 import ru.ansmos.filmoteka.R[m
 import ru.ansmos.filmoteka.db.Film[m
 [m
[36m@@ -17,7 +18,14 @@[m [mclass FilmViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemV[m
     //В этом методе кладем данные из Film в наши View[m
     fun bund(film: Film){[m
         title.text = film.title[m
[31m-        poster.setImageResource(film.poster)[m
[32m+[m[32m        //poster.setImageResource(film.poster) Оставил на память[m
[32m+[m[32m        Glide.with(itemView)[m
[32m+[m[32m            //Загружаем сам ресурс[m
[32m+[m[32m            .load(film.poster)[m
[32m+[m[32m            //Центруем изображение[m
[32m+[m[32m            .centerCrop()[m
[32m+[m[32m            //Указываем ImageView, куда будем загружать изображение[m
[32m+[m[32m            .into(poster)[m
         desc.text = film.description[m
     }[m
 }[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/res/layout/fragment_home.xml b/app/src/main/res/layout/fragment_home.xml[m
[1mindex 280f673..8ec9fef 100644[m
[1m--- a/app/src/main/res/layout/fragment_home.xml[m
[1m+++ b/app/src/main/res/layout/fragment_home.xml[m
[36m@@ -1,59 +1,14 @@[m
 <?xml version="1.0" encoding="utf-8"?>[m
[31m-<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
[31m-    xmlns:app="http://schemas.android.com/apk/res-auto"[m
[32m+[m[32m<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
     xmlns:tools="http://schemas.android.com/tools"[m
[31m-    android:id="@+id/coordinator_layout"[m
[32m+[m[32m    android:id="@+id/home_fragment_root"[m
     android:layout_width="match_parent"[m
     android:layout_height="match_parent"[m
[31m-    android:fitsSystemWindows="true"[m
[32m+[m[32m    tools:background="@color/teal_200"[m
     tools:context=".HomeFragment">[m
 [m
[31m-    <com.google.android.material.appbar.AppBarLayout[m
[31m-        android:id="@+id/topAppBarLayout"[m
[31m-        android:layout_width="match_parent"[m
[31m-        android:layout_height="wrap_content"[m
[31m-        android:background="@color/cardview_dark_background"[m
[31m-        android:fitsSystemWindows="true">[m
[32m+[m[32m</androidx.constraintlayout.widget.ConstraintLayout>[m
 [m
[31m-        <com.google.android.material.appbar.CollapsingToolbarLayout[m
[31m-            android:id="@+id/toolbar_layout"[m
[31m-            android:layout_width="match_parent"[m
[31m-            android:layout_height="match_parent"[m
[31m-            app:layout_scrollFlags="scroll|enterAlways"[m
[31m-            app:toolbarId="@+id/toolbar">[m
[31m-[m
[31m-            <androidx.appcompat.widget.SearchView[m
[31m-                android:id="@+id/search_view"[m
[31m-                android:layout_width="match_parent"[m
[31m-                android:layout_height="wrap_content"[m
[31m-                android:background="@drawable/background_search_view"[m
[31m-                android:layout_margin="@dimen/m27.layout_margin.search">[m
[31m-[m
[31m-            </androidx.appcompat.widget.SearchView>[m
[31m-[m
[31m-        </com.google.android.material.appbar.CollapsingToolbarLayout>[m
[31m-    </com.google.android.material.appbar.AppBarLayout>[m
[31m-[m
[31m-[m
[31m-    <androidx.core.widget.NestedScrollView[m
[31m-        xmlns:android="http://schemas.android.com/apk/res/android"[m
[31m-        xmlns:app="http://schemas.android.com/apk/res-auto"[m
[31m-        xmlns:tools="http://schemas.android.com/tools"[m
[31m-        android:id="@+id/main_container"[m
[31m-        android:layout_width="match_parent"[m
[31m-        android:layout_height="match_parent"[m
[31m-        app:layout_behavior="@string/appbar_scrolling_view_behavior"[m
[31m-        android:background="@drawable/corner_background"[m
[31m-        tools:context=".HomeFragment">[m
[31m-[m
[31m-    <androidx.recyclerview.widget.RecyclerView[m
[31m-        android:id="@+id/main_recycler"[m
[31m-        android:layout_width="match_parent"[m
[31m-        android:layout_height="match_parent"[m
[31m-        tools:listitem="@layout/film_item"/>[m
[31m-    </androidx.core.widget.NestedScrollView>[m
[31m-[m
[31m-</androidx.coordinatorlayout.widget.CoordinatorLayout>[m
 [m
 [m
 [m
[1mdiff --git a/app/src/main/res/layout/merge_home_screen_content.xml b/app/src/main/res/layout/merge_home_screen_content.xml[m
[1mnew file mode 100644[m
[1mindex 0000000..f9d8ea3[m
[1m--- /dev/null[m
[1m+++ b/app/src/main/res/layout/merge_home_screen_content.xml[m
[36m@@ -0,0 +1,61 @@[m
[32m+[m[32m<?xml version="1.0" encoding="utf-8"?>[m
[32m+[m[32m<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
[32m+[m[32m    android:layout_width="match_parent"[m
[32m+[m[32m    android:layout_height="match_parent">[m
[32m+[m
[32m+[m[32m    <androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
[32m+[m[32m        xmlns:app="http://schemas.android.com/apk/res-auto"[m
[32m+[m[32m        xmlns:tools="http://schemas.android.com/tools"[m
[32m+[m[32m        android:id="@+id/coordinator_layout"[m
[32m+[m[32m        android:layout_width="match_parent"[m
[32m+[m[32m        android:layout_height="match_parent"[m
[32m+[m[32m        android:fitsSystemWindows="true">[m
[32m+[m
[32m+[m[32m        <com.google.android.material.appbar.AppBarLayout[m
[32m+[m[32m            android:id="@+id/topAppBarLayout"[m
[32m+[m[32m            android:layout_width="match_parent"[m
[32m+[m[32m            android:layout_height="wrap_content"[m
[32m+[m[32m            android:background="@color/cardview_dark_background"[m
[32m+[m[32m            android:fitsSystemWindows="true">[m
[32m+[m
[32m+[m[32m            <com.google.android.material.appbar.CollapsingToolbarLayout[m
[32m+[m[32m                android:id="@+id/toolbar_layout"[m
[32m+[m[32m                android:layout_width="match_parent"[m
[32m+[m[32m                android:layout_height="match_parent"[m
[32m+[m[32m                app:layout_scrollFlags="scroll|enterAlways"[m
[32m+[m[32m                app:toolbarId="@+id/toolbar">[m
[32m+[m
[32m+[m[32m                <androidx.appcompat.widget.SearchView[m
[32m+[m[32m                    android:id="@+id/search_view"[m
[32m+[m[32m                    android:layout_width="match_parent"[m
[32m+[m[32m                    android:layout_height="wrap_content"[m
[32m+[m[32m                    android:background="@drawable/background_search_view"[m
[32m+[m[32m                    android:layout_margin="@dimen/m27.layout_margin.search">[m
[32m+[m
[32m+[m[32m                </androidx.appcompat.widget.SearchView>[m
[32m+[m
[32m+[m[32m            </com.google.android.material.appbar.CollapsingToolbarLayout>[m
[32m+[m[32m        </com.google.android.material.appbar.AppBarLayout>[m
[32m+[m
[32m+[m
[32m+[m[32m        <androidx.core.widget.NestedScrollView[m
[32m+[m[32m            xmlns:android="http://schemas.android.com/apk/res/android"[m
[32m+[m[32m            xmlns:app="http://schemas.android.com/apk/res-auto"[m
[32m+[m[32m            xmlns:tools="http://schemas.android.com/tools"[m
[32m+[m[32m            android:id="@+id/main_container"[m
[32m+[m[32m            android:layout_width="match_parent"[m
[32m+[m[32m            android:layout_height="match_parent"[m
[32m+[m[32m            app:layout_behavior="@string/appbar_scrolling_view_behavior"[m
[32m+[m[32m            android:background="@drawable/corner_background"[m
[32m+[m[32m            tools:context=".HomeFragment">[m
[32m+[m
[32m+[m[32m            <androidx.recyclerview.widget.RecyclerView[m
[32m+[m[32m                android:id="@+id/main_recycler"[m
[32m+[m[32m                android:layout_width="match_parent"[m
[32m+[m[32m                android:layout_height="match_parent"[m
[32m+[m[32m                tools:listitem="@layout/film_item"/>[m
[32m+[m[32m        </androidx.core.widget.NestedScrollView>[m
[32m+[m
[32m+[m[32m    </androidx.coordinatorlayout.widget.CoordinatorLayout>[m
[32m+[m
[32m+[m[32m</androidx.constraintlayout.widget.ConstraintLayout>[m
\ No newline at end of file[m
