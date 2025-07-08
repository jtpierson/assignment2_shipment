package org.example.project.observer

interface Observer<T> {
    fun update(subject: T)
}