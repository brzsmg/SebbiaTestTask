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
import com.sebbia.brzsmg.testtask.models.Category
import com.sebbia.brzsmg.testtask.models.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

/**
 * Фрагмент подробного описания новости.
 */
class NewsDetails : Fragment() {

    //Parameters
    private lateinit var mCategory : Category
    private lateinit var mNews : News

    //Views
    private lateinit var mvCategoryName : TextView
    private lateinit var mvDate : TextView
    private lateinit var mvTitle : TextView
    private lateinit var mvShortDescription : TextView
    private lateinit var mvFullDescription : TextView

    //Data
    private var mRequest : Disposable? = null

    companion object {
        fun newInstance(category : Category, news : News) : NewsDetails {
            val fragment = NewsDetails()
            val arguments = Bundle()
            arguments.putSerializable("category", category)
            arguments.putSerializable("news", news)
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
            mNews = arguments?.getSerializable("news") as News
        }
        if(savedInstanceState != null) {
            mNews = savedInstanceState.getSerializable("news") as News
        }

        activity?.title = "Подробности"
        val view = inflater.inflate(R.layout.fragment_news_details, container, false)
        mvCategoryName = view.findViewById(R.id.category_name)
        mvDate = view.findViewById(R.id.date)
        mvTitle = view.findViewById(R.id.title)
        mvShortDescription = view.findViewById(R.id.short_description)
        mvFullDescription = view.findViewById(R.id.full_description)
        mvFullDescription.movementMethod = LinkMovementMethod.getInstance()

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("news", mNews)
    }

    override fun onStart() {
        super.onStart()
        showData(false)
        if(mNews.fullDescription == null) {
            requestData()
        }
    }

    private fun showData(request: Boolean) {
        if(!request) {
            mvCategoryName.text = mCategory.name
            mvDate.text = mNews.date.toFormat("dd.MM.yyyy hh:mm")
            mvTitle.text = mNews.title
            mvShortDescription.text = mNews.shortDescription
        }
        if(mNews.fullDescription != null) {
            mvFullDescription.text =
                Html.fromHtml(mNews.fullDescription, Html.FROM_HTML_MODE_COMPACT)
        }
    }

    private fun requestData() {
        mRequest?.dispose()
        mRequest = app.newsApi.requestNewsDetails(mNews.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                if(result.isSuccessful) {
                    if(result.body()?.news != null) {
                        mNews = result.body()?.news!!
                        showData(true)
                    }
                } else {
                    Toast.makeText(activity,"Ошибка.", Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                if(error is IOException) {
                    Toast.makeText(
                        activity,
                        "Ошибка.\r\nПроверьте подключение к сети.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Исключение: " + error.javaClass.simpleName,
                        Toast.LENGTH_SHORT
                    ).show()
                    error.printStackTrace()
                }
            })
    }
}