package com.khs.nbbang.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator

object FragmentUtils{

    fun loadFragment(fragment: Fragment, container_id:Int, fragmentManager: FragmentManager) {
        loadFragment(fragment, container_id, fragmentManager, false)
    }

    fun loadFragment(fragment: Fragment, container_id:Int, fragmentManager: FragmentManager, isSavedBackStack: Boolean) {
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

    fun currentFragment(fragmentManager: FragmentManager, container_id: Int): Fragment? {
        return fragmentManager.findFragmentById(container_id)
    }

    fun currentFragmentClassName(fragmentContainerView: FragmentContainerView) : String{
        return (fragmentContainerView.findNavController().currentDestination as FragmentNavigator.Destination).className
    }

}

fun FragmentManager.clearBackStack() {
    LogUtil.dLog("TEST","FragmentUtils", " clearBackStack ext > ")
    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}