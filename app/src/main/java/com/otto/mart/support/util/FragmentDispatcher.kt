package com.otto.mart.support.util

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.otto.mart.R
import com.otto.mart.ui.fragment.AppFragment

class FragmentDispatcher(private val host: FragmentActivity) : LifecycleObserver {
    private val lifeCycle: Lifecycle? = host.lifecycle
    private var fragmentPending: AppFragment? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        fragmentPending?.let { showFragment(it) }
    }

    fun dispatchFragment(frag: AppFragment) {
        if (lifeCycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) == true) {
            showFragment(frag)
        } else {
            fragmentPending = frag
        }
    }

    private fun showFragment(frag: AppFragment) {
        host.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContainer, frag)
                .disallowAddToBackStack()
                .commit()

        fragmentPending = null
    }
}