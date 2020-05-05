package com.sebbia.brzsmg.testtask.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sebbia.brzsmg.testtask.R
import com.sebbia.brzsmg.testtask.adapters.NewsAdapter
import com.sebbia.brzsmg.testtask.app
import com.sebbia.brzsmg.testtask.models.Category
import com.sebbia.brzsmg.testtask.models.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

/**
 * Фрагмент списка новостей.
 * С пагинацией.
 */
class NewsListFragment : Fragment() {

    companion object {

        private const val START_PAGE: Int = 0
        private const val END_PAGE: Int = -1

        fun newInstance(category : Category) : NewsListFragment {
            val fragment = NewsListFragment()
            val arguments = Bundle()
            arguments.putSerializable("category", category)
            fragment.arguments = arguments
            return fragment
        }
    }

    //Parameters
    private lateinit var mCategory : Category

    //Views and Adapters
    private lateinit var mvRefreshList : SwipeRefreshLayout
    private lateinit var mvMessage : TextView
    private lateinit var mvList : RecyclerView
    private lateinit var mLayoutManager : LinearLayoutManager
    private lateinit var mAdapter : NewsAdapter

    //Data
    private var mData : ArrayList<News> = ArrayList()
    private var mRequest : Disposable? = null
    private var mPage : Int = START_PAGE
    private var mLoading : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            mCategory = arguments?.getSerializable("category") as Category
        }
        if(savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            val data = savedInstanceState.getSerializable("data") as ArrayList<News>
            mData.addAll(data)
            mPage = savedInstanceState.getInt("page")
            mLoading = savedInstanceState.getBoolean("loading")
        }

        activity?.title = mCategory.name
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)
        mvRefreshList = view.findViewById(R.id.refresh_list)
        mvMessage = view.findViewById(R.id.message)
        mvList = view.findViewById(R.id.list)
        mLayoutManager = LinearLayoutManager(activity)
        mvList.layoutManager = mLayoutManager

        mAdapter = NewsAdapter(mData) { news ->
            val bundle = Bundle()
            bundle.putSerializable("category", mCategory)
            bundle.putSerializable("news", news)
            findNavController().navigate(R.id.action_details, bundle)
        }
        mvList.adapter = mAdapter

        mvRefreshList.setOnRefreshListener {
            mData.clear()
            mAdapter.notifyDataSetChanged()
            mPage = START_PAGE
            requestNextPage()
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("data", mData)
        outState.putInt("page", mPage)
        outState.putBoolean("loading", mLoading)
    }

    override fun onStart() {
        super.onStart()
        mvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val total = mLayoutManager.itemCount
                val currentLastItem: Int = mLayoutManager.findLastVisibleItemPosition()
                if (currentLastItem == total - 1) {
                    requestNextPage()
                }
            }
        })
        if(mPage == START_PAGE) {
            requestNextPage()
        } else {
            mvMessage.visibility = View.GONE
            mvList.visibility = View.VISIBLE
        }
    }

    fun requestNextPage() {
        if(mPage == END_PAGE) {
            return
        }
        if(mLoading) {
            return
        }
        mLoading = true

        mvMessage.text = ""
        mvRefreshList.isRefreshing = true
        if(mPage  < (START_PAGE + 1) ) {
            mvMessage.visibility = View.VISIBLE
            mvList.visibility = View.GONE
        }
        Log.i("PAGINATION", "Запос страницы $mPage")
        mRequest?.dispose()
        mRequest = app.newsApi.requestCategory(mCategory.id, mPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                if(result.isSuccessful) {
                    val list = result.body()?.list!!
                    if (list.count() > 0) {
                        val start = mData.count()
                        mData.addAll(list)
                        mAdapter.notifyItemRangeInserted(start, list.count())
                        mPage++
                    } else {
                        Toast.makeText(activity, "Данных больше нет.", Toast.LENGTH_SHORT)
                            .show()
                        mPage = -1
                    }
                } else {
                    Toast.makeText(activity,"Ошибка.", Toast.LENGTH_SHORT).show()
                }
                mLoading = false
                mvRefreshList.isRefreshing = false
                if(mvMessage.isVisible) {
                    mvMessage.visibility = View.GONE
                    mvList.visibility = View.VISIBLE
                }
            }, { error ->
                if(error is IOException) {
                    mvMessage.setText(R.string.server_unavailable)
                } else {
                    Log.e("API", "Исключение: " + error.javaClass.simpleName)
                    if (error.message != null) {
                        Log.e("API", error.message as String)
                    }
                    error.printStackTrace()
                    mvMessage.text = getString(R.string.exception_of, error.javaClass.simpleName)
                }
                mLoading = false
                mvRefreshList.isRefreshing = false
            })

    }

}