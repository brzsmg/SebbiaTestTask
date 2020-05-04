package com.sebbia.brzsmg.testtask.activities

import android.os.Bundle
import com.sebbia.brzsmg.testtask.R
import com.sebbia.brzsmg.testtask.fragments.CategoriesFragment
import com.sebbia.brzsmg.testtask.ui.FragmentsActivity

/**
 * Главная активность.
 */
class MainActivity : FragmentsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            setNextFragment(CategoriesFragment())
        }
    }
}
