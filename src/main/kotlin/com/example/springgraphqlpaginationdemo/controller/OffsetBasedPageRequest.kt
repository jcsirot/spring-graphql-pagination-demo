package com.example.springgraphqlpaginationdemo.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort


class OffsetBasedPageRequest(limit: Int, offset: Int) : Pageable {
    private val limit: Int
    private val offset: Int

    // Constructor could be expanded if sorting is needed
    private val sort = Sort.by(Sort.Direction.ASC, "id")

    init {
        require(limit >= 1) { "Limit must not be less than one!" }
        require(offset >= 0) { "Offset index must not be less than zero!" }
        this.limit = limit
        this.offset = offset
    }

    override fun getPageNumber(): Int {
        return offset / limit
    }

    override fun getPageSize(): Int {
        return limit
    }

    override fun getOffset(): Long {
        return offset.toLong()
    }

    override fun getSort(): Sort {
        return sort
    }

    override fun next(): Pageable {
        // Typecast possible because number of entries cannot be bigger than integer (primary key is integer)
        return OffsetBasedPageRequest(pageSize, (getOffset() + pageSize).toInt())
    }

    fun previous(): Pageable {
        // The integers are positive. Subtracting does not let them become bigger than integer.
        return if (hasPrevious()) OffsetBasedPageRequest(pageSize, (getOffset() - pageSize).toInt()) else this
    }

    override fun previousOrFirst(): Pageable {
        return if (hasPrevious()) previous() else first()
    }

    override fun first(): Pageable {
        return OffsetBasedPageRequest(pageSize, 0)
    }

    override fun withPage(pageNumber: Int): Pageable {
        TODO("Not yet implemented")
    }

    override fun hasPrevious(): Boolean {
        return offset > limit
    }
}

