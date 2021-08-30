package com.khs.nbbang.utils

import androidx.constraintlayout.motion.widget.MotionLayout

fun MotionLayout.setTransitionListener(
    change : ((transitionName : String?) -> Unit),
    started: ((start: Int, end: Int) -> Unit),
    completion: ((state: Int) -> Unit)
) {
    setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

        }

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            started.invoke(p1, p2)
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            change.invoke(p0?.transitionName)
        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            completion.invoke(p1)
        }
    })
}
