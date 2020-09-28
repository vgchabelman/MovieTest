package br.eloibrito.com.movie_db.recycle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.eloibrito.com.movie_db.R
import br.eloibrito.com.movie_db.models.Movies
import br.eloibrito.com.movie_db.network.Callback
import br.eloibrito.com.movie_db.network.EndPoint
import br.eloibrito.com.movie_db.utils.CarregaImagemDaUrl
import java.util.ArrayList

class RecycleMovies(private val context: Context, private var mLista: List<Movies>) :
    RecyclerView.Adapter<RecycleMovies.ViewHolder>(), Filterable {

    internal var lista_temp: List<Movies>? = null

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        internal var image: ImageView = v.findViewById<View>(R.id.imagens) as ImageView
        internal var cView: CardView = v.findViewById<View>(R.id.c_view) as CardView

    }

    init {
        lista_temp = mLista
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_movies, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        CarregaImagemDaUrl.carregaImagen(context, holder.image, String.format("%s/%s", EndPoint.image_movie, mLista[position].poster_path))
        holder.cView.setOnClickListener(click(position, holder.image))
    }


    override fun getItemCount(): Int {
        return mLista.size
    }

    fun click(position: Int, v: ImageView): View.OnClickListener {
        return View.OnClickListener { v ->

            val callback = context as Callback
            callback.onRetornoMovie(mLista[position], v)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                try {
                    if (charSequence != null && charSequence.isNotEmpty()) {
                        val charString = charSequence.toString().toLowerCase()
                        val filtrado = ArrayList<Movies>()
                        for (movies in lista_temp!!)
                            if (movies.original_title!!.toLowerCase().contains(charString) || movies.title!!.toLowerCase().contains(
                                    charString
                                )
                            )
                                filtrado.add(movies)


                        filterResults.count = filtrado.size
                        filterResults.values = filtrado
                    } else {
                        filterResults.count = lista_temp!!.size
                        filterResults.values = lista_temp
                    }

                } catch (err: Exception) {
                    err.printStackTrace()
                }

                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                try {
                    mLista = filterResults.values as ArrayList<Movies>
                    notifyDataSetChanged()
                } catch (err: Exception) {
                    err.printStackTrace()
                }

            }
        }
    }
}