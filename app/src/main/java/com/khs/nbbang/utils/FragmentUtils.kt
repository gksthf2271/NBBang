package com.khs.nbbang.utils

import androidx.fragment.app.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator

open class FragmentUtils{

    constructor()

    open fun loadFragment(fragment: Fragment, container_id:Int, fragmentManager: FragmentManager) {
        loadFragment(fragment, container_id, fragmentManager, false)
    }

    open fun loadFragment(fragment: Fragment, container_id:Int, fragmentManager: FragmentManager, isSavedBackStack: Boolean) {
        val className: String = fragment.javaClass.simpleName
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (isSavedBackStack) {
            fragmentTransaction.replace(container_id, fragment, className)
            fragmentTransaction.addToBackStack(null)
        } else {
            fragmentTransaction.replace(container_id, fragment, className)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    open fun currentFragment(fragmentManager: FragmentManager, container_id: Int): Fragment? {
        return fragmentManager.findFragmentById(container_id)
    }

    open fun currentFragmentClassName(fragmentContainerView: FragmentContainerView) : String{
        return (fragmentContainerView.findNavController().currentDestination as FragmentNavigator.Destination).className
    }

}

fun FragmentManager.clearBackStack() {
    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}