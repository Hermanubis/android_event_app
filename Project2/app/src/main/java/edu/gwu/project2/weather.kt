package edu.gwu.project2

import java.io.Serializable

data class weather (
    val city: String,
    val condition: String,
    val low: String,
    val high: String,
    val img : String
): Serializable