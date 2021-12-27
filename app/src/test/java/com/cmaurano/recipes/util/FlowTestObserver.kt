package com.cmaurano.recipes.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import java.io.Closeable

fun <T> Flow<T>.test(scope: CoroutineScope): FlowTestObserver<T> {
    return FlowTestObserver(scope, this)
}

class FlowTestObserver<T> internal constructor(
    scope: CoroutineScope,
    flow: Flow<T>,
) : Closeable {

    private val _values = mutableListOf<T>()
    val values: List<T>
        get() = _values

    private val job: Job = scope.launch {
        flow.collect { _values.add(it) }
    }

    fun assertNoValues(): FlowTestObserver<T> {
        assertEquals(emptyList<T>(), _values)
        return this
    }

    fun assertValues(vararg values: T): FlowTestObserver<T> {
        assertEquals(values.toList(), _values)
        return this
    }

    fun assertValueCount(count: Int): FlowTestObserver<T> {
        assertEquals(count, _values.size)
        return this
    }

    fun finish() {
        close()
    }

    override fun close() {
        job.cancel()
    }
}