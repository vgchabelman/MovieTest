package br.eloibrito.com.movie_db.network

import android.view.View
import br.eloibrito.com.movie_db.models.Movies

interface Callback {

    fun onRetornoMovie(page : Int?)
    fun onRetornoMovie(movies : Movies, v: View)
    fun onClear()
}