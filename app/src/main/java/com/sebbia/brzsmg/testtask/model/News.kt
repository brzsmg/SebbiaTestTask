package com.sebbia.brzsmg.testtask.model

import com.sebbia.brzsmg.testtask.types.DateTime

/**
 * Сущность новости.
 */
class News {
    var id: Int? = null
    var date: DateTime? = null
    var fullDescription: String? = null
    var shortDescription: String? = null
    var title: String? = null
}