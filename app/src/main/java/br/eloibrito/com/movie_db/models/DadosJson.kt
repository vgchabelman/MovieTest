package br.eloibrito.com.movie_db.models

import com.google.gson.annotations.SerializedName

class DadosJson {

    @SerializedName("page")
    var page : Int ? = null
    @SerializedName("total_pages")
    var total_pages : Int ? = null

    @SerializedName("genres")
    var lista_generos : ArrayList<Generos>? = null
    @SerializedName("results")
    var lista_movies : ArrayList<Movies>? = null
}