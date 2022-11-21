package com.dustrean.api.tasks

import lombok.Getter
import lombok.Setter

@Setter
@Getter
abstract class SchedulerTask<T> constructor(
    val scheduler: Scheduler<SchedulerTask<T>, RepeatSchedulerTask<*>>
){

    private var task: T? = null
    private var id: Int = -1

    abstract fun cancel()

}