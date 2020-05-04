package com.sebbia.brzsmg.testtask.models

import com.sebbia.brzsmg.testtask.types.DateTime
import java.io.Serializable

/**
 * Сущность новости.
 */
class News (
    val id: Int,
    val date: DateTime,
    val title: String,
    val shortDescription: String,
    val fullDescription: String?
) : Serializable
