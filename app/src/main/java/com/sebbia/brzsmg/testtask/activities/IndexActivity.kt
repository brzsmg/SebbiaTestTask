package com.sebbia.brzsmg.testtask.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Стартовая активность.
 */
class IndexActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Здесь делаем загрузку, проверяем авторизацию, настройки подключения и тд
        //Если все хорошо переходим к главной активности
        var intent = Intent(this@IndexActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}