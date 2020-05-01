package com.sebbia.brzsmg.testtask.fragments

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sebbia.brzsmg.testtask.R
import com.sebbia.brzsmg.testtask.app
import com.sebbia.brzsmg.testtask.model.Category
import com.sebbia.brzsmg.testtask.model.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Фрагмент подробного описания новости.
 */
class NewsDetails(
    val category : Category,
    val news : News
) : Fragment() {
    lateinit var mvCategoryName : TextView
    lateinit var mvDate : TextView
    lateinit var mvTitle : TextView
    lateinit var mvShortDescription : TextView
    lateinit var mvFullDescription : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Подробности")
        val view = inflater.inflate(R.layout.fragment_news_details, container, false)
        mvCategoryName = view.findViewById(R.id.category_name)
        mvDate = view.findViewById(R.id.date)
        mvTitle = view.findViewById(R.id.title)
        mvShortDescription = view.findViewById(R.id.short_description)
        mvFullDescription = view.findViewById(R.id.full_description)
        mvFullDescription.movementMethod = LinkMovementMethod.getInstance()
        return view
    }

    override fun onStart() {
        super.onStart()
        mvCategoryName.text = category.name
        mvDate.text = news.date?.toFormat("dd.MM.yyyy hh:mm")
        mvTitle.text = news.title
        mvShortDescription.text = news.shortDescription
        requestData()
    }

    fun requestData() {
        val observer = app.newsApi.requestNewsDetails(news.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                if(result.isSuccessful) {
                    if(result.body()?.news != null) {
                        val html = Html.fromHtml(result.body()?.news?.fullDescription, Html.FROM_HTML_MODE_COMPACT)
                        mvFullDescription.text = html

                    }
                } else {
                    Toast.makeText(activity,"Ошибка.", Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Toast.makeText(activity,"Исключение: " + error.javaClass.simpleName, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            })
    }
}