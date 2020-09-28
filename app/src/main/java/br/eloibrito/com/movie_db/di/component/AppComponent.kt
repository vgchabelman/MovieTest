package br.eloibrito.com.movie_db.di.component

import br.eloibrito.com.movie_db.ListMovieModel
import br.eloibrito.com.movie_db.di.module.ModuloApp
import br.eloibrito.com.movie_db.di.module.RetrofitModule
import br.eloibrito.com.movie_db.utils.App
import dagger.Component

@Component(modules = [ModuloApp::class, RetrofitModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(movies: ListMovieModel)
}