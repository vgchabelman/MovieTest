package br.eloibrito.com.movie_db.models

import com.google.gson.annotations.SerializedName

class Generos {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null

    var checked : Boolean? = false
}