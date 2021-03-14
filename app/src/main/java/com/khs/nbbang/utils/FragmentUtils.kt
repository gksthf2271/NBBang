package com.khs.nbbang.utils

import androidx.fragment.app.*
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.khs.nbbang.base.BaseFragment

open class FragmentUtils{

    constructor()

    open fun loadFragment(fragment: Fragment, container_id:Int, fragmentManager: FragmentManager) {
        loadFragment(fragment, container_id, fragmentManager, false)
    }

    open fun loadFragment(fragment: Fragment, container_id:Int, fragmentManager: FragmentManager, isAdded: Boolean) {
        val className: String = fragment.javaClass.simpleName
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        if (isAdded) {
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.add(container_id, fragment, className)
        } else {
            fragmentTransaction.replace(container_id, fragment, className)
        }
        fragmentTransaction.commit()
    }

    open fun currentFragment(fragmentManager: FragmentManager, container_id: Int): Fragment? {
        return fragmentManager.findFragmentById(container_id)
    }

    open fun currentFragmentClassName(fragmentContainerView: FragmentContainerView) : String{
        return (fragmentContainerView.findNavController().currentDestination as FragmentNavigator.Destination).className
    }
}