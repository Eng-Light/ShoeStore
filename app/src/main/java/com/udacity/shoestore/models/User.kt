package com.udacity.shoestore.models

data class User(
    val email: String,
    val shoes: MutableList<Shoe> = mutableListOf()
)
