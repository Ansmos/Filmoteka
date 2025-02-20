package ru.ansmos.filmoteka.data

import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PositionalDataSource
import ru.ansmos.filmoteka.db.Film

//class InfoStorage {
//    fun getData(startPosition: Int, loadSize: Int): MutableList<Film> {
//        val endPos = minOf(startPosition + loadSize - 1, 100)
//        return (startPosition..endPos).map { Film(it + 1, "Info num ${it + 1}") } as MutableList<Info>
//    }
//}

class FakeRepo {

    fun getDataDSFactory(startPosition :Int, loadSize : Int) : DataSource.Factory<Int, Film>{
        val list = films.subList(startPosition, startPosition + loadSize) as MutableList<Film>
        return list as DataSource.Factory<Int, Film>
    }

    fun getDataDSFactoryRx() : DataSource.Factory<Int, Film>{
        val list = films as MutableList<Film>
        return list as DataSource.Factory<Int, Film>
    }

    fun getData(startPosition :Int, loadSize : Int) : MutableList<Film>{
        val list = films.subList(startPosition, startPosition + loadSize) as MutableList<Film>
        return list
    }

     private val films = listOf<Film>(
        Film("tt3896198","Первый",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов", "2024",),
        Film("tt3896198","Второй",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", "2024", 1.5),
        Film("tt3896198","Третий",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", "2024", 9.0, true),
        Film("tt3896198","Четвертый",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master.", "2024"),
        Film("tt3896198","Пятый",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. ", "2024"),
        Film("tt3896198","Шестой",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук)).", "2024"),
        Film("tt3896198","Седьмой",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи.", "2024"),
        Film("tt3896198","Восьмой",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает.", "2024"),
        Film("tt3896198","Девятый",
            "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
            "Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ", "2024"),

                Film("tt3896198","Первый 2",
         "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
         "В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов", "2024"),
    Film("tt3896198","Второй 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", "2024", 1.5),
    Film("tt3896198","Третий 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", "2024", 9.0, true),
    Film("tt3896198","Четвертый 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master.", "2024"),
    Film("tt3896198","Пятый 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. ", "2024"),
    Film("tt3896198","Шестой 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук)).", "2024"),
    Film("tt3896198","Седьмой 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи.", "2024"),
    Film("tt3896198","Восьмой 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает.", "2024"),
    Film("tt3896198","Девятый 2",
    "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
    "Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ", "2024"),

         Film("tt3896198","Первый 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов", "2024"),
         Film("tt3896198","Второй 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", "2024", 1.5),
         Film("tt3896198","Третий 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", "2024", 9.0, true),
         Film("tt3896198","Четвертый 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master.", "2024"),
         Film("tt3896198","Пятый 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. ", "2024"),
         Film("tt3896198","Шестой 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук)).", "2024"),
         Film("tt3896198","Седьмой 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи.", "2024"),
         Film("tt3896198","Восьмой 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает.", "2024"),
         Film("tt3896198","Девятый 3",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ", "2024"),

         Film("tt3896198","Первый 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов", "2024"),
         Film("tt3896198","Второй 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", "2024", 1.5),
         Film("tt3896198","Третий 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", "2024", 9.0, true),
         Film("tt3896198","Четвертый 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master.", "2024"),
         Film("tt3896198","Пятый 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. ", "2024"),
         Film("tt3896198","Шестой 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук)).", "2024"),
         Film("tt3896198","Седьмой 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи.", "2024"),
         Film("tt3896198","Восьмой 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает.", "2024"),
         Film("tt3896198","Девятый 4",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ", "2024"),

         Film("tt3896198","Первый 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов", "2024"),
         Film("tt3896198","Второй 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", "2024", 1.5),
         Film("tt3896198","Третий 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", "2024", 9.0, true),
         Film("tt3896198","Четвертый 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master.", "2024"),
         Film("tt3896198","Пятый 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. ", "2024"),
         Film("tt3896198","Шестой 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук)).", "2024"),
         Film("tt3896198","Седьмой 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи.", "2024"),
         Film("tt3896198","Восьмой 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает.", "2024"),
         Film("tt3896198","Девятый 5",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ", "2024"),

         Film("tt3896198","Первый 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "В целом все должно быть понятно, за исключением, может, поля poster. Там мы будем хранить id картинки в ресурсах, а как вы помните, они у нас в Int. Теперь наступает «творческая» часть: нужно подготовить 7–10 фильмов", "2024"),
         Film("tt3896198","Второй 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Смысл такой: когда проект только начинается, вы сразу делаете новую ветку от master → ветку develop и от неё уже делаете ветки с новым функционалом (feature) или исправлением неработающего кода (bugfix).", "2024", 1.5),
         Film("tt3896198","Третий 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "После того как вы сделали свою feature , вы это мержите в develop (после ревью начальника, когда будете работать, в нашем случае, желательно, после проверки ментора).", "2024", 9.0, true),
         Film("tt3896198","Четвертый 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "И вот таким образом вы создаёте приложение, пока не будет понимания, что можно его показать людям, и вы тогда создаёте release ветку (ставится ещё соответствующий тэг, но на этом сейчас останавливаться не будем), мержите изменения в неё (в случае с коммерческой разработкой в этом моменте она передаётся на тестирование) и потом уже мержите в master.", "2024"),
         Film("tt3896198","Пятый 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Осталось все наши предыдущие ветки с проектными заданиями смержить в develop (если у вас их проверили, конечно), придерживайтесь очередности их создания. ", "2024"),
         Film("tt3896198","Шестой 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Нам нужно создать RecyclerView для главной страницы с фильмами (пока у нас нет данных по фильмам из сети, будем использовать mock-данные, то есть вручную создадим нашу БД с фильмами (7 - 10 штук)).", "2024"),
         Film("tt3896198","Седьмой 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Начнём мы не с этого. А с темы системы контроля версий. Когда вы успешно пройдете курс, успешно пройдете собеседование и получите работу, вы столкнетесь с тем, что в разработке, к сожалению, не все так просто, как хотелось бы, и там недостаточно просто делать ветки, коммиты и мержи.", "2024"),
         Film("tt3896198","Восьмой 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Мера это вынужденная, ведь над проектом может работать сколько угодно человек, и надо все это как-то структурировать. Не будем углубляться в глубокие глубины, у вас и так сейчас голова в режиме терминатора работает.", "2024"),
         Film("tt3896198","Девятый 6",
             "https://m.media-amazon.com/images/M/MV5BZWViZDRiMGYtZTlmMS00ZDM2LWIzNWUtYmNjYzBiYjJkNTk3XkEyXkFqcGc@._V1_SX300.jpg",
             "Всё должно быть понятно, возможно, будет в новинку атрибут tools: он нужен для того, чтобы были какие-то представления на момент верстки, чтобы визуально легче было ориентироваться. ", "2024")
     )




}
