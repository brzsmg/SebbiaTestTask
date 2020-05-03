package com.sebbia.brzsmg.testtask.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sebbia.brzsmg.testtask.R
import com.sebbia.brzsmg.testtask.adapters.NewsAdapter
import com.sebbia.brzsmg.testtask.app
import com.sebbia.brzsmg.testtask.model.Category
import com.sebbia.brzsmg.testtask.model.News
import com.sebbia.brzsmg.testtask.ui.FragmentsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Фрагмент списка новостей.
 * С пагинацией.
 */
class NewsListFragment(val category : Category) : Fragment() {
    lateinit var mvList : RecyclerView
    lateinit var mLayoutManager : LinearLayoutManager
    var mData : ArrayList<News> = ArrayList()
    lateinit var mAdapter : NewsAdapter
    var page : Int = 0
    var loading : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(category.name)
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)
        mvList = view.findViewById(R.id.list)
        mLayoutManager = LinearLayoutManager(activity)
        mvList.layoutManager = mLayoutManager

        mAdapter = NewsAdapter(mData) { news ->
            (activity as FragmentsActivity).setNextFragment(NewsDetails(category, news))
        }
        mvList.adapter = mAdapter

        return view
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
        requestNextPage()
    }

    fun requestNextPage() {
        if(page < 0) {
            return
        }
        if(loading) {
            return
        }
        loading = true
        Log.i("PAGINATION","Запос страницы " + page)
        val observer = app.newsApi.requestCategory(category.id!!, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                if(result.isSuccessful) {
                    if(result.body()?.list!!.count() > 0) {
                        page++
                        mData.addAll(result.body()?.list!!)
                        mAdapter.notifyDataSetChanged() //TODO: не оптимальное использование notify
                    } else {
                        page = -1
                        Toast.makeText(activity,"Данных больше нет.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity,"Ошибка.", Toast.LENGTH_SHORT).show()
                }
                loading = false
            }, { error ->
                Toast.makeText(activity,"Исключение: " + error.javaClass.simpleName, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
                loading = false
            })

    }

}