package ru.ansmos.filmoteka.data

import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.db.Film

class MainRepository {
    val filmsDataBase = listOf<Film>(
    Film(1,"Первый", R.drawable.poster_1,"В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов"),
    Film(2,"Второй",
    R.drawable.poster_2,"Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", 1.5f),
    Film(3,"Третий",
    R.drawable.poster_3,"После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", 9f, true),
    Film(4,"Четвертый", R.drawable.poster_4,"И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master."),
    Film(5,"Пятый", R.drawable.poster_5,"Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. "),
    Film(6,"Шестой", R.drawable.poster_6,"Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук))."),
    Film(7,"Седьмой", R.drawable.poster_7,"Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи."),
    Film(8,"Восьмой", R.drawable.poster_8,"Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает."),
    Film(9,"Девятый", R.drawable.poster_9,"Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ")
    )
}