package org.example.project.observer

interface Subject {
    fun registerObserver(observer : Observer)
    fun removeObserver(observer : Observer)
    fun notifyObservers()
}