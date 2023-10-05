package com.example.springgraphqlpaginationdemo.controller

import com.example.springgraphqlpaginationdemo.domain.Book
import com.example.springgraphqlpaginationdemo.repository.BookRepository
import org.springframework.data.domain.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.query.ScrollSubrange
import org.springframework.stereotype.Controller
import java.util.function.IntFunction


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

        val offset = scrollSubrange.position().map { s -> (s as OffsetScrollPosition).offset + 1 }.orElse(0).toInt()
        val count = scrollSubrange.count().orElse(25)
        val pageRequest = OffsetBasedPageRequest(count, offset)
        val result = bookRepository.findAll(pageRequest)
        val positionFunction = IntFunction { index ->
            OffsetScrollPosition.of(index.toLong() + offset)
        }
        return Window.from(result.content, positionFunction, result.hasNext())
    }
}
