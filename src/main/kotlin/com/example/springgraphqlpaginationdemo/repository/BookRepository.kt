package com.example.springgraphqlpaginationdemo.repository

import com.example.springgraphqlpaginationdemo.domain.Book
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Window
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Int>