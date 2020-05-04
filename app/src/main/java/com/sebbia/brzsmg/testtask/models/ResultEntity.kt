package com.sebbia.brzsmg.testtask.models

/**
 * Результат API, со списком.
 */
class ResultEntity<T> (
    val code : Int,
    val list : List<T>
)
