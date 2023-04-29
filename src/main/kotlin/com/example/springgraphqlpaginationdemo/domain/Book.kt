package com.example.springgraphqlpaginationdemo.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Book(
    @Id
    val id: Int,

    val name: String,

    val author: String
)
