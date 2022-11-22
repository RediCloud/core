package com.dustrean.api.tasks


abstract class RepeatSchedulerTask<T> constructor(
    schedule: Scheduler<SchedulerTask<T>, RepeatSchedulerTask<*>>
) : SchedulerTask<T>(schedule) {
    private val filters: MutableList<TaskFilter> = mutableListOf()

    var asyncFilters: Boolean = false

    fun addFilter(filter: TaskFilter): TaskFilter {
        filters.add(filter)
        return filter
    }

    fun filter(): Boolean {
        return filters.all { it.filter() }
    }

    fun filterAsync(consumer: (Boolean) -> Unit) {
        if (filters.isEmpty()) {
            consumer(true)
            return
        }
        this.scheduler.runTaskAsync {
            consumer(filter())
        }
    }
}