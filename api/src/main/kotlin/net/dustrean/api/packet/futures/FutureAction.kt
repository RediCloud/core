package net.dustrean.api.packet.futures

import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeoutException
import kotlin.time.Duration

class FutureAction<T>() : CompletableFuture<T>() {
    var result: T? = null
    var throwable: Throwable? = null

    constructor(result: T) : this() {
        complete(result)
        this.result = result
    }

    constructor(throwable: Throwable) : this() {
        completeExceptionally(throwable)
        this.throwable = throwable
    }

    fun isFinishedAnyway(): Boolean =
        (isDone || isCancelled || isCompletedExceptionally) || (result != null || throwable != null)

    fun getBlockOrNull(): T? {
        return try {
            result = get()
            result
        } catch (e: Exception) {
            throwable = e
            null
        }
    }

    fun orTimeout(duration: Duration): FutureAction<T> {
        val future = FutureAction<T>()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (!isDone && !isCancelled && !isCompletedExceptionally) future.completeExceptionally(TimeoutException())
            }
        }, duration.inWholeMilliseconds)
        return future
    }

    fun onFailure(futureAction: FutureAction<T>): FutureAction<T> {
        if (result != null) return this
        if (throwable != null) {
            futureAction.completeExceptionally(throwable!!)
            return futureAction
        }
        futureAction.whenComplete { _, throwable ->
            if (throwable == null) return@whenComplete
            this.throwable = throwable
            completeExceptionally(throwable)
        }
        return this
    }

    fun onFailure(consumer: (Throwable) -> Unit): FutureAction<T> {
        if (result != null) return this
        whenComplete { _, throwable ->
            if (throwable == null) return@whenComplete
            consumer(throwable)
        }
        return this
    }

    fun onSuccess(consumer: (T) -> Unit): FutureAction<T> {
        if (result != null) consumer(result!!)
        if (throwable != null) return this
        whenComplete { result, _ ->
            if (result == null) return@whenComplete
            this.result = result
            complete(result)
        }
        return this
    }

    fun <R> map(mapper: FutureMapper<T, R>): FutureAction<R> {
        val futureAction = FutureAction<R>()
        whenComplete { result, throwable ->
            if (throwable != null) {
                this.throwable = throwable
                futureAction.completeExceptionally(throwable)
                return@whenComplete
            }

            this.result = result
            futureAction.complete(mapper.map(result))
        }
        return futureAction
    }

    override fun complete(value: T): Boolean {
        result = value
        return super.complete(value)
    }

    override fun completeExceptionally(ex: Throwable?): Boolean {
        throwable = ex
        return super.completeExceptionally(ex)
    }
}