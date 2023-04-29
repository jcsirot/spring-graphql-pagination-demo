package com.example.springgraphqlpaginationdemo.controller

import com.example.springgraphqlpaginationdemo.domain.Book
import com.example.springgraphqlpaginationdemo.repository.BookRepository
import java.util.function.IntFunction
import org.springframework.data.domain.OffsetScrollPosition
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Window
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.query.ScrollSubrange
import org.springframework.stereotype.Controller

@Controller
class BookController(
    private val bookRepository: BookRepository
) {

    @QueryMapping
    fun book(@Argument id: Int): Book {
        return bookRepository.findById(id).get()
    }

    @QueryMapping
    fun books(scrollSubrange: ScrollSubrange): Window<Book> {
        val offset = (scrollSubrange.position().orElse(OffsetScrollPosition.initial()) as OffsetScrollPosition).offset.toInt()
        val count = scrollSubrange.count().orElse(25)
        val pageRequest = PageRequest.of(offset, count)
        val result = bookRepository.findAll(pageRequest)
        val positionFunction = IntFunction { index ->
            OffsetScrollPosition.of(index.toLong())
        }
        return Window.from(result.content, positionFunction, result.hasNext())
    }
}
