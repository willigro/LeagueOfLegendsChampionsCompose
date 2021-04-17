package com.rittmann.leagueoflegendschamps.base.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private var mObserverPriority: Observer<in T>? = null
    private val mPendingPriority = AtomicBoolean(false)

    private val mPending = AtomicBoolean(false)

    fun observePriority(owner: LifecycleOwner, observer: Observer<in T>) {
        mObserverPriority = observer
        observe(owner, observer)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (mObserverPriority == null || owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t)
                }
            } else {
                if (mPendingPriority.compareAndSet(true, false)) {
                    mObserverPriority!!.onChanged(t)
                }
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        mPendingPriority.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {

        private val TAG = "SingleLiveEvent"
    }
}