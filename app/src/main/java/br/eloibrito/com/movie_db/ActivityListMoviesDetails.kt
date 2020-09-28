package br.eloibrito.com.movie_db

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import br.eloibrito.com.movie_db.models.Movies
import br.eloibrito.com.movie_db.network.EndPoint
import br.eloibrito.com.movie_db.utils.CarregaImagemDaUrl
import br.eloibrito.com.movie_db.utils.CheckReadPermission
import kotlinx.android.synthetic.main.layout_list_movie_details.*
import java.lang.StringBuilder


class ActivityListMoviesDetails : AppCompatActivity() {

    companion object {

        val ID_DETAIL = "detail:_id"
        val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_list_movie_details)

        if (!CheckReadPermission.show(this))
            return

        controles()

    }

    private fun controles() {

         ViewCompat.setTransitionName(
            img,
            VIEW_NAME_HEADER_IMAGE
        )

        val movies = intent.getSerializableExtra(ID_DETAIL) as Movies

        if(movies != null) {


            var sbHtml = StringBuilder()
            sbHtml.append(String.format("<font color='red'>%s</font> %s", resources.getString(R.string.detalhes_nota), movies.vote_average))
            sbHtml.append(String.format("<br/><font color='red'>%s</font> %s", resources.getString(R.string.detalhes_data), movies.release_date))
            sbHtml.append(String.format("<br/><font color='red'>%s</font> %s", resources.getString(R.string.detalhes_linguagem_original), movies.original_language))
            sbHtml.append(String.format("<br/><font color='red'>%s</font> %s", resources.getString(R.string.detalhes_total_votos), movies.vote_count))
            sbHtml.append(String.format("<br/><font color='red'>%s</font> %s", resources.getString(R.string.detalhes_popularidade), movies.popularity))

            txt_title.text = String.format("%s", movies.title)
            txt_details.text = Html.fromHtml(sbHtml.toString())
            txt_details_about.text = String.format("%s", movies.overview)


            CarregaImagemDaUrl.carregaImagen(this, img, String.format("%s/%s", EndPoint.image_movie, movies.backdrop_path))
            //img.setImageBitmap(CarregaImagemDaUrl.retornaImagem(this, String.format("%s/%s", EndPoint.image_movie, movies.poster_path)))
           // CarregaImagemDaUrl.buscar(this, String.format("%s/%s", EndPoint.image_movie, movies.poster_path), img, progress)
        }

        img.setOnClickListener { v ->
            CarregaImagemDaUrl.carregaImagen(this, img, String.format("%s/%s", EndPoint.image_movie, movies.backdrop_path)) }
    }
}