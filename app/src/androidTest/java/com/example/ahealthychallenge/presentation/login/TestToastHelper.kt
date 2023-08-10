package com.example.ahealthychallenge.presentation.login

import com.example.ahealthychallenge.presentation.ToastHelper

class TestToastHelper : ToastHelper {

    private val shownToasts = mutableListOf<String>()

    override fun showToast(message: String) {
        shownToasts.add(message)
    }

    fun hasToast(message: String): Boolean {
        return shownToasts.contains(message)
    }
}