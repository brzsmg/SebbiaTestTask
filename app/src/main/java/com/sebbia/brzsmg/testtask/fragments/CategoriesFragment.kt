package com.sebbia.brzsmg.testtask.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sebbia.brzsmg.testtask.R
import com.sebbia.brzsmg.testtask.adapters.CategoryAdapter
import com.sebbia.brzsmg.testtask.app
import com.sebbia.brzsmg.testtask.models.Category
import com.sebbia.brzsmg.testtask.ui.FragmentsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

/**
 * Фрагмент со списком категорий.
 */
class CategoriesFragment : Fragment() {

    //Views and Adapters
    private lateinit var mvRefreshList : SwipeRefreshLayout
    private lateinit var mvMessage : TextView
    private lateinit var mvList : RecyclerView
    private lateinit var mAdapter : CategoryAdapter

    //Data
    private val mData : ArrayList<Category> = ArrayList()
    private var mRequest : Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Категории"
        if(savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            val data = savedInstanceState.getSerializable("data") as ArrayList<Category>
            mData.addAll(data)
        }

        val view = inflater.inflate(R.layout.fragment_categoriy_list, container, false)
        mvRefreshList = view.findViewById(R.id.refresh_list)
        mvMessage = view.findViewById(R.id.message)
        mvList = view.findViewById(R.id.list)
        mvList.layoutManager = LinearLayoutManager(activity)

        mAdapter = CategoryAdapter(mData) { category ->
            (activity as FragmentsActivity).setNextFragment(NewsListFragment.newInstance(category))
        }
        mvList.adapter = mAdapter

        mvRefreshList.setOnRefreshListener {
            mData.clear()
            mAdapter.notifyDataSetChanged()
            requestData()
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("data", mData)
    }

    override fun onStart() {
        super.onStart()
        if(mData.count() == 0) {
            requestData()
        } else {
            mvMessage.visibility = View.GONE
            mvList.visibility = View.VISIBLE
        }
    }

    private fun requestData() {
        mvMessage.text = ""
        mvRefreshList.isRefreshing = true
        mvMessage.visibility = View.VISIBLE
        mvList.visibility = View.GONE
        mRequest?.dispose()
        mRequest = app.newsApi.requestCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                if(result.isSuccessful) {
                    val list = result.body()?.list as List<Category>
                    val start = mData.count()
                    mData.addAll(list)
                    mAdapter.notifyItemRangeInserted(start, list.count())
                } else {
                    Toast.makeText(activity,"Нет данных.", Toast.LENGTH_SHORT).show()
                }
                mvRefreshList.isRefreshing = false
                mvMessage.visibility = View.GONE
                mvList.visibility = View.VISIBLE
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
                mvRefreshList.isRefreshing = false
            })
    }
}