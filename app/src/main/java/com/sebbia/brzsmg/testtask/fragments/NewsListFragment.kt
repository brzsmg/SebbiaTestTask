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
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Фрагмент списка новостей.
 * С пагинацией.
 */
class NewsListFragment : Fragment() {

    //Parameters
    private lateinit var mCategory : Category

    //Views
    private lateinit var mvList : RecyclerView
    private lateinit var mLayoutManager : LinearLayoutManager

    //Data
    private var mRequest : Disposable? = null
    private var mData : ArrayList<News> = ArrayList()
    private lateinit var mAdapter : NewsAdapter
    private var mPage : Int = 0
    private var mLoading : Boolean = false


    companion object {
        fun newInstance(category : Category) : NewsListFragment {
            val fragment = NewsListFragment()
            val arguments = Bundle()
            arguments.putSerializable("category", category)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            mCategory = arguments?.getSerializable("category") as Category
        }
        activity?.title = mCategory.name
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)
        mvList = view.findViewById(R.id.list)
        mLayoutManager = LinearLayoutManager(activity)
        mvList.layoutManager = mLayoutManager

        mAdapter = NewsAdapter(mData) { news ->
            (activity as FragmentsActivity).setNextFragment(NewsDetails.newInstance(mCategory, news))
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
        if(mPage < 0) {
            return
        }
        if(mLoading) {
            return
        }
        mLoading = true
        Log.i("PAGINATION", "Запос страницы $mPage")
        mRequest?.dispose()
        mRequest = app.newsApi.requestCategory(mCategory.id, mPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                if(result.isSuccessful) {
                    val list = result.body()?.list!!
                    if(list.count() > 0) {
                        mPage++
                        val start = mData.count()
                        mData.addAll(list)
                        mAdapter.notifyItemRangeInserted(start, list.count())
                    } else {
                        mPage = -1
                        Toast.makeText(activity,"Данных больше нет.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity,"Ошибка.", Toast.LENGTH_SHORT).show()
                }
                mLoading = false
            }, { error ->
                Toast.makeText(activity,"Исключение: " + error.javaClass.simpleName, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
                mLoading = false
            })

    }

}