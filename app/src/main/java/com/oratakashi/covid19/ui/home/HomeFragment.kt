package com.oratakashi.covid19.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Sessions
import com.oratakashi.covid19.data.model.instagram.DataMedia
import com.oratakashi.covid19.data.model.news.DataNews
import com.oratakashi.covid19.root.App
import com.oratakashi.covid19.ui.home.local_module.LocalState.Error
import com.oratakashi.covid19.ui.home.local_module.LocalState.Loading
import com.oratakashi.covid19.ui.home.local_module.LocalState.Result
import com.oratakashi.covid19.ui.home.dialog.HomeDialogFragment
import com.oratakashi.covid19.ui.home.dialog.HomeDialogInterface
import com.oratakashi.covid19.ui.home.global_module.GlobalState
import com.oratakashi.covid19.ui.home.global_module.GlobalViewModel
import com.oratakashi.covid19.ui.home.local_module.LocalViewModel
import com.oratakashi.covid19.ui.home.news_module.*
import com.oratakashi.covid19.ui.main.v2.GlobalDetailActivity
import com.oratakashi.covid19.ui.webview.WebViewActivity
import com.oratakashi.covid19.utils.Converter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : DaggerFragment(), HomeDialogInterface,
    InstagramInterface, NewsInterface {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val localData : LocalViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LocalViewModel::class.java)
    }
    val globalData : GlobalViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(GlobalViewModel::class.java)
    }

    val newsData : NewsViewModel by lazy{
        ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)
    }

    val dataInstagram : MutableList<DataMedia> = ArrayList()

    val adapterInstagram : InstagramAdapter by lazy{
        InstagramAdapter(
            dataInstagram, this
        )
    }

    val dataNews : MutableList<DataNews> = ArrayList()

    val adapterNews : NewsAdapter by lazy {
        NewsAdapter(dataNews, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        tvTotalCase.text = "${resources.getString(R.string.title_new_case)} di ${tvLocation.text}"

        rvInstagram.adapter = adapterInstagram
        rvInstagram.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        rvNews.adapter = adapterNews
        rvNews.layoutManager = LinearLayoutManager(requireContext())

        setupViewModel()
    }

    fun setupViewModel(){
        localData.state.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is Loading -> {
                       when(localData.checkData()){
                           true -> {
                               val result = localData.countData()

                               tvConfirmedLocal.text = Converter.numberConvert(result.confirm!!)
                               tvRecoveredLocal.text = Converter.numberConvert(result.recovered!!)
                               tvDeathLocal.text = Converter.numberConvert(result.death!!)
                               tvLastUpdate.text = result.date
                           }
                           false -> {
                               llLoadingLocal.visibility = View.VISIBLE
                               llHeader.visibility = View.GONE
                           }
                       }
                    }
                    is Result -> {
                        llLoadingLocal.visibility = View.GONE
                        llHeader.visibility = View.VISIBLE

                        localData.cacheData(it.data)
                        val result = localData.countData()

                        tvConfirmedLocal.text = Converter.numberConvert(result.confirm!!)
                        tvRecoveredLocal.text = Converter.numberConvert(result.recovered!!)
                        tvDeathLocal.text = Converter.numberConvert(result.death!!)
                        tvLastUpdate.text = result.date
                    }
                    is Error -> {
                        llLoadingLocal.visibility = View.GONE
                        llHeader.visibility = View.VISIBLE

                        val result = localData.countData()

                        tvConfirmedLocal.text = Converter.numberConvert(result.confirm!!)
                        tvRecoveredLocal.text = Converter.numberConvert(result.recovered!!)
                        tvDeathLocal.text = Converter.numberConvert(result.death!!)
                        tvLastUpdate.text = result.date

                        Toast.makeText(context, "Gagal mendapatkan data terbaru!",
                            Toast.LENGTH_SHORT).show()

                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        globalData.state.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is GlobalState.Loading -> {
                        llLoadingGlobal.visibility = View.VISIBLE
                        llGlobal.visibility = View.GONE
                    }
                    is GlobalState.Result -> {
                        llLoadingGlobal.visibility = View.GONE
                        llGlobal.visibility = View.VISIBLE

                        App.sessions!!.putInt(Sessions.last_confirmed, it.data.confirmed!!.value)
                        App.sessions!!.putInt(Sessions.last_recovered, it.data.recovered!!.value)
                        App.sessions!!.putInt(Sessions.last_death, it.data.deaths!!.value)

                        tvConfirmedGlobal.text = Converter.numberConvert(
                            App.sessions!!.getInt(Sessions.last_confirmed)
                        )
                        tvRecoveredGlobal.text = Converter.numberConvert(
                            App.sessions!!.getInt(Sessions.last_recovered)
                        )
                        tvDeathGlobal.text = Converter.numberConvert(
                            App.sessions!!.getInt(Sessions.last_death)
                        )
                    }
                    is GlobalState.Error -> {
                        llLoadingGlobal.visibility = View.GONE
                        llGlobal.visibility = View.VISIBLE

                        tvConfirmedGlobal.text = Converter.numberConvert(
                            App.sessions!!.getInt(Sessions.last_confirmed)
                        )
                        tvRecoveredGlobal.text = Converter.numberConvert(
                            App.sessions!!.getInt(Sessions.last_recovered)
                        )
                        tvDeathGlobal.text = Converter.numberConvert(
                            App.sessions!!.getInt(Sessions.last_death)
                        )

                        Toast.makeText(context, "Gagal mendapatkan data terbaru!",
                            Toast.LENGTH_SHORT).show()

                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        newsData.state_ig.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is InstagramState.Loading -> {
                        shInstagram.visibility = View.VISIBLE
                        shInstagram.startShimmerAnimation()
                        rvInstagram.visibility = View.GONE
                    }
                    is InstagramState.Result -> {
                        shInstagram.stopShimmerAnimation()
                        shInstagram.visibility = View.GONE
                        rvInstagram.visibility = View.VISIBLE

                        dataInstagram.clear()
                        dataInstagram.addAll(it.data.data.media!!)
                        adapterInstagram.notifyDataSetChanged()
                    }
                    is InstagramState.Error -> {
                        shInstagram.stopShimmerAnimation()
                        shInstagram.visibility = View.GONE
                        rvInstagram.visibility = View.VISIBLE

                        Toast.makeText(context, "Gagal mendapatkan data instagram!",
                            Toast.LENGTH_SHORT).show()

                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        newsData.state_news.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is NewsState.Loading -> {
                        llLoading.visibility = View.VISIBLE
                        rvNews.visibility = View.GONE
                    }
                    is NewsState.Result -> {
                        llLoading.visibility = View.GONE
                        rvNews.visibility = View.VISIBLE

                        dataNews.clear()
                        dataNews.addAll(it.data.data)
                        adapterNews.notifyDataSetChanged()
                    }
                    is NewsState.Error -> {
                        llLoading.visibility = View.GONE
                        rvNews.visibility = View.VISIBLE
                        Toast.makeText(context, "Gagal mendapatkan data instagram!",
                            Toast.LENGTH_SHORT).show()

                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        localData.getData()
        globalData.getData()
        newsData.getInstagram()
        newsData.getNews()
    }

    override fun onSelected(province: String) {
        when(province.isNotEmpty()){
            true -> {
                val result = localData.countData(province)

                tvConfirmedLocal.text = Converter.numberConvert(result.confirm!!)
                tvRecoveredLocal.text = Converter.numberConvert(result.recovered!!)
                tvDeathLocal.text = Converter.numberConvert(result.death!!)
                tvLocation.text = province

                tvTotalCase.text = "${resources.getString(R.string.title_new_case)} di ${tvLocation.text}"
            }
            false -> {
                val result = localData.countData()

                tvConfirmedLocal.text = Converter.numberConvert(result.confirm!!)
                tvRecoveredLocal.text = Converter.numberConvert(result.recovered!!)
                tvDeathLocal.text = Converter.numberConvert(result.death!!)
                tvLocation.text = resources.getString(R.string.title_indonesia)

                tvTotalCase.text = "${resources.getString(R.string.title_new_case)} di ${tvLocation.text}"
            }
        }
    }

    override fun onPostClick(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
    }

    override fun onNewsClick(link: String) {
        val i = Intent(context, WebViewActivity::class.java)
        i.putExtra("url", link)
        startActivity(i)
    }

    @OnClick(R.id.rlLocation) fun onLocation(){
        HomeDialogFragment.newInstance(this).show(childFragmentManager, "dialog")
    }

    @OnClick(R.id.cvGlobal) fun onGlobal(){
        startActivity(Intent(context, GlobalDetailActivity::class.java))
    }
}
