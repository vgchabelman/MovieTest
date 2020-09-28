package br.eloibrito.com.movie_db.recycle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.eloibrito.com.movie_db.ActivityListMovies
import br.eloibrito.com.movie_db.R
import br.eloibrito.com.movie_db.models.Generos

class RecycleGeneros(private val context: Context, private val mLista: List<Generos>) :
    RecyclerView.Adapter<RecycleGeneros.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        internal var generos: TextView = v.findViewById<View>(R.id.generos) as TextView

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_generos, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.generos.setBackgroundResource(
            if (mLista[position].checked!!) R.drawable.border_color_text else R.drawable.border_color_text_normal

        )

        holder.generos.text = mLista[position].name
        holder.generos.setOnClickListener(click(position))
    }


    override fun getItemCount(): Int {
        return mLista.size
    }

    fun click(position: Int): View.OnClickListener {
        return View.OnClickListener { v ->
            mLista[position].checked = !mLista[position].checked!!
            notifyItemChanged(position)

            val activity = context as ActivityListMovies
            activity.onRetornoMovie(1)
            activity.onClear()

        }
    }
}