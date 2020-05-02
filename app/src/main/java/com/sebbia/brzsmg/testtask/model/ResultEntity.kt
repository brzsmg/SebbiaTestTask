package com.sebbia.brzsmg.testtask.model

/**
 * Результат API, со списком.
 */
class ResultEntity<T> (
    val code : Int,
    val list : List<T>
)
