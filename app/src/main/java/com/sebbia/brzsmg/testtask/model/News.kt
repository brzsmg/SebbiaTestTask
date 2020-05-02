package com.sebbia.brzsmg.testtask.model

import com.sebbia.brzsmg.testtask.types.DateTime

/**
 * Сущность новости.
 */
class News (
    val id: Int,
    val date: DateTime,
    val fullDescription: String,
    val shortDescription: String,
    val title: String
)
