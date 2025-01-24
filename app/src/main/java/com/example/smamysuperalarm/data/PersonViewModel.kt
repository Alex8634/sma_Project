package com.example.smamysuperalarm.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonViewModel(application: Application): AndroidViewModel(application) {
    private val getAll: List<Model_Person>
    private val repository : PersonRepository

    init{
        val personDao = PersonDatabase.getDb(application).personDao()
        repository = PersonRepository(personDao)
        getAll = repository.getAll
    }

    fun addPerson(person: Model_Person){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPerson(person)
        } //run la cod in background
    }
}