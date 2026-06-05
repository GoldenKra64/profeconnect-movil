package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow(
        User(
            name = "",
            email = ""
        )
    )

    val user = _user.asStateFlow()

    fun changeName(name: String) {
        _user.value = _user.value.copy(
            name = name
        )
    }

    fun changeEmail(email: String) {
        _user.value = _user.value.copy(
            email = email
        )
    }

    fun saveUser() {

        val currentUser = _user.value

        println(currentUser.name)
        println(currentUser.email)

        // repository.save(currentUser)
    }
}