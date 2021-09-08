package dev.gressier.pennydrop

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

// Adapted from here: https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04
fun <T> LiveData<T>.getOrAwaitValue(time: Long = 2, timeUnit: TimeUnit = SECONDS): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(newValue: T) {
            data = newValue
            latch.countDown()
            removeObserver(this)
        }
    }
    observeForever(observer)
    if (!latch.await(time, timeUnit)) {
        removeObserver(observer)
        return null
    }
    return data
}