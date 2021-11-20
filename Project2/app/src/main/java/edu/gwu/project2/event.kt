package edu.gwu.project2

import java.io.Serializable

data class event (
    val name: String,
    val time: String,
    val img: String,
    val venue: String,
    val link : String,
    val info: String
    ): Serializable