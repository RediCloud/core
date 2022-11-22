package com.dustrean.api.tasks

abstract class SchedulerTask<T> constructor(
    val scheduler: Scheduler<SchedulerTask<T>, RepeatSchedulerTask<*>>
){

    var task: T? = null

    var id: Int = -1

    abstract fun cancel()

}