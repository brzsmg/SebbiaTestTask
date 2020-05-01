package com.sebbia.brzsmg.testtask.model

/**
 * Результат API, со списком.
 */
class ResultEntity<T> {
    var code : Int? = null
    var list : List<T>? = null
}