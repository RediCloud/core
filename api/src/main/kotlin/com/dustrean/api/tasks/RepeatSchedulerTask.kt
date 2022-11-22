package com.dustrean.api.tasks

import java.util.function.Consumer

abstract class RepeatSchedulerTask<T> constructor(
    schedule: Scheduler<SchedulerTask<T>, RepeatSchedulerTask<*>>
): SchedulerTask<T>(schedule) {

    private val filters: MutableList<TaskFilter> = mutableListOf()

    var asyncFilters: Boolean = false

    fun addFilter(filter: TaskFilter): TaskFilter {
        filters.add(filter)
        return filter
    }

    fun filter(): Boolean {
        return filters.all { it.filter() }
    }

    fun filterAsync(consumer: Consumer<Boolean>) {
        if(filters.isEmpty()) {
            consumer.accept(true)
            return
        }
        this.scheduler.runTaskAsync {
            consumer.accept(filter())
        }
    }
}