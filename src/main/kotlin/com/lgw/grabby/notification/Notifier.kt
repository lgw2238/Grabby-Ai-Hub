package com.lgw.grabby.notification


interface Notifier {
    fun notify(subject: String, message: String)
}