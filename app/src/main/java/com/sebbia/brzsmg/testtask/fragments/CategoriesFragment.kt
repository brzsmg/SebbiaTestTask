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
import com.sebbia.brzsmg.testtask.adapters.CategoryAdapter
import com.sebbia.brzsmg.testtask.app
import com.sebbia.brzsmg.testtask.model.Category
import com.sebbia.brzsmg.testtask.ui.FragmentsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Фрагмент со списком категорий.
 */
class CategoriesFragment : Fragment() {

    lateinit var mvList : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Категории")
        val view = inflater.inflate(R.layout.fragment_categoriy_list, container, false)
        mvList = view.findViewById(R.id.list)
        mvList.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onStart() {
        super.onStart()
        val observer = app.newsApi.requestCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                if(result.isSuccessful) {
                    val adapter = CategoryAdapter(result.body()?.list as List<Category>) { category ->
                        (activity as FragmentsActivity).setNextFragment(NewsListFragment(category))
                        //Toast.makeText(activity, response.name, Toast.LENGTH_SHORT).show()
                    }
                    mvList.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity,"Нет данных.", Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Log.e("API", "Исключение: " + error.javaClass.simpleName)
                if (error.message != null) {
                    Log.e("API", error.message as String)
                }
                Toast.makeText(activity,"Исключение: " + error.javaClass.simpleName, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            })
    }
}