package br.eloibrito.com.movie_db

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.eloibrito.com.movie_db.models.Generos
import br.eloibrito.com.movie_db.models.Movies
import br.eloibrito.com.movie_db.network.Callback
import br.eloibrito.com.movie_db.recycle.PaginationScrolled
import br.eloibrito.com.movie_db.recycle.RecycleGeneros
import br.eloibrito.com.movie_db.recycle.RecycleMovies
import br.eloibrito.com.movie_db.utils.CheckReadPermission
import br.eloibrito.com.movie_db.utils.Utils
import kotlinx.android.synthetic.main.layout_list_movies.*
import java.lang.Exception

class ActivityListMovies : AppCompatActivity(), Callback, View.OnClickListener, SearchView.OnQueryTextListener {

    //var REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124

    lateinit var viewModel: ListMovieModel
    private var lista_generos = ArrayList<Generos>()
    private var lista_movies = ArrayList<Movies>()
    private var page : Int? = 1
    var isLastPage: Boolean = false
    var isLoading: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_list_movies)

        if (!CheckReadPermission.show(this))
            return

        controles()

    }

    fun controles() {

        toolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(ListMovieModel::class.java)

        /*toolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(toolbar)*/

        fab_atualizar.setOnClickListener(this)
        call_generos()
    }


    @SuppressLint("RestrictedApi")
    fun call_generos() {
        val mLayoutManagerG =
            LinearLayoutManager(this@ActivityListMovies, LinearLayoutManager.HORIZONTAL, false)
        val mLayoutManagerM = GridLayoutManager(this, 2)

        val recycle_generos = findViewById<RecyclerView>(R.id.recycle_generos)
        recycle_generos.setHasFixedSize(true)
        recycle_generos.layoutManager = mLayoutManagerG
        //var divider = DividerItemDecoration(applicationContext, DividerItemDecoration.HORIZONTAL)
        //divider.setDrawable(resources.getDrawable(R.drawable.divider))
        //recycle_generos.addItemDecoration(divider)

        val recycle_movies = findViewById<RecyclerView>(R.id.recycle_movies)
        recycle_movies.setHasFixedSize(true)
        recycle_movies.layoutManager = mLayoutManagerM



        val mRecycleGeneros = RecycleGeneros(
            this,
            lista_generos
        )

        val mRecycleMovies = RecycleMovies(
            this,
            lista_movies
        )

        recycle_generos.adapter = mRecycleGeneros
        recycle_movies.adapter = mRecycleMovies

        recycle_movies?.addOnScrollListener(object : PaginationScrolled(mLayoutManagerM) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun carregarMaisItens() {
                isLoading = true

                onRetornoMovie(page!!.inc())
            }
        })


        viewModel.buscarGeneros(applicationContext, page!!)

        viewModel._isLoading.observe(this, Observer {
            if (it!!) progress.visibility = View.VISIBLE else progress.visibility = View.GONE
        })

        viewModel._isMessageError.observe(this, Observer { messagemDialog ->
            // if (messagemDialog.isNotEmpty()) loading?.dialogText(messagemDialog)
        })

        viewModel._listaGeneros.observe(this, Observer { it ->
            lista_generos.clear()
            lista_generos.addAll(viewModel.get_lista_generos())
            mRecycleGeneros.notifyDataSetChanged()

        })

        viewModel._listaMovies.observe(this, Observer { it ->
            //lista_movies.clear()
            lista_movies.addAll(viewModel.get_lista_movies())
            mRecycleMovies.notifyDataSetChanged()
            isLoading = false
            isLastPage = false

            toolbar.subtitle = resources.getString(R.string.toolbar_sub_title).replace("_TOTAL", String.format("%s", lista_movies.size))
        })

        viewModel._isMessageError.observe(this, Observer { messagemDialog ->
            if (messagemDialog.isNotEmpty())  {
                Utils.mensagemSnack(
                    principal,
                    messagemDialog,
                    R.color.vermelho,
                    R.drawable.ic_fechar
                )
                fab_atualizar.visibility = View.VISIBLE
            } else
                fab_atualizar.visibility = View.GONE

        })

        viewModel._page.observe(this, Observer { p -> this.page = p })
    }

    override fun onRetornoMovie(page : Int?) {
        val generos_selecionados = ArrayList<Int>()
        for (generos in lista_generos)
            if (generos.checked!!) {
                Log.e("GENEROS_SELECIONADOS", generos.id!!.toString())
                generos_selecionados.add(generos.id!!)
            }


        isLastPage = true
        viewModel.buscarFilmes(generos_selecionados.toIntArray(), applicationContext, page!!)
    }

    override fun onRetornoMovie(movies: Movies, v: View) {

        val intent = Intent(this@ActivityListMovies, ActivityListMoviesDetails::class.java)
        intent.putExtra(ActivityListMoviesDetails.ID_DETAIL, movies)


        val activityOptions: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@ActivityListMovies,


                Pair<View, String>(
                    v,
                    ActivityListMoviesDetails.VIEW_NAME_HEADER_IMAGE
                )
            )

        ActivityCompat.startActivity(this@ActivityListMovies, intent, activityOptions.toBundle())

    }

    override fun onClear() {
        lista_movies.clear()
        val adapter = recycle_movies.adapter
        adapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        v!!.visibility = View.GONE

        viewModel.buscarGeneros(applicationContext, page!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_buscar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.buscar).actionView as SearchView


        searchView?.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )

        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        try {
            isLastPage = false
            if(p0!!.isNotEmpty())
                isLastPage = true

            val adapter = recycle_movies.adapter as RecycleMovies
            adapter.filter.filter(p0)
            return true
        } catch (err: Exception) {
            err.printStackTrace()
        }

        return false
    }

}