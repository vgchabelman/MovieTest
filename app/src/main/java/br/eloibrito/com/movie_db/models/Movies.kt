package br.eloibrito.com.movie_db.models

import java.io.Serializable

class Movies : Serializable{

    var id : Long? = null

    var popularity : Float? = null
    var vote_count : Int? = null
    var video : Boolean? = null
    var poster_path : String? = null
    var adult : Boolean? = null
    var backdrop_path : String? = null
    var original_language : String? = null
    var original_title : String? = null
    var genre_ids : IntArray? = null
    var title : String? = null
    var vote_average : Float? = null
    var overview : String? = null
    var release_date : String? = null

    var status_img : Boolean? = false

}