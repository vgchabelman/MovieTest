package br.eloibrito.com.movie_db

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.eloibrito.com.movie_db.models.Generos
import br.eloibrito.com.movie_db.models.Movies
import br.eloibrito.com.movie_db.network.ApiRetrofit
import br.eloibrito.com.movie_db.network.EndPoint
import br.eloibrito.com.movie_db.utils.App
import br.eloibrito.com.movie_db.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ListMovieModel() : ViewModel() {

    @Inject
    lateinit var api: ApiRetrofit
    val _isLoading = MutableLiveData<Boolean>()
    val _isMessageError = MutableLiveData<String>()
    val _listaGeneros = MutableLiveData<List<Generos>>()
    val _listaMovies = MutableLiveData<List<Movies>>()
    val _page = MutableLiveData<Int>()

    init {
        (App).getComponent().inject(this)

        _isLoading.postValue(false)
        _listaMovies.value = ArrayList<Movies>()

    }


    @SuppressLint("CheckResult")
    fun buscarGeneros(context: Context, page: Int) {

        val generos = api.get_generos(EndPoint.chave_api)

        generos.subscribeOn(IoScheduler()).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { resultado ->

                    if (!resultado.lista_generos.isNullOrEmpty()) {
                        _listaGeneros.postValue(resultado.lista_generos)
                        var generos = IntArray(0)
                        buscarFilmes(generos, context, page)
                    }
                },
                { error ->
                    _isMessageError.postValue(context.resources.getString(R.string.erro_generos))
                }
            )
    }

    @SuppressLint("CheckResult")
    fun buscarFilmes(generos: IntArray, context: Context, page: Int) {
        ativa_desativa_loading(true)

        val generos_implode = Utils.implodeString(generos, ",")

        val movies = api.get_movies_page(generos_implode, page, EndPoint.chave_api)


        movies.subscribeOn(IoScheduler()).subscribeOn(AndroidSchedulers.mainThread())
            .doFinally { ativa_desativa_loading(false) }
            .subscribe(
                { resultado ->
                    if (!resultado.lista_movies.isNullOrEmpty()) {
                        _page.postValue(page)
                        _listaMovies.postValue(resultado.lista_movies)
                    }

                    ativa_desativa_loading(false)
                },
                { error ->
                    _isMessageError.postValue(context.resources.getString(R.string.erro_filmes))

                }
            )
    }


    fun ativa_desativa_loading(status: Boolean) = _isLoading.postValue(status)

    fun get_lista_generos(): List<Generos> = _listaGeneros.value!!
    fun get_lista_movies(): List<Movies> = _listaMovies.value!!
}