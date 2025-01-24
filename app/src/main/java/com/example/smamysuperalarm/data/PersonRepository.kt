package com.example.smamysuperalarm.data

class PersonRepository(private val personDao: PersonDao) {

    val getAll: List<Model_Person> = personDao.getAll()

    fun getPersonByname(nume: String)
    {
        personDao.getPersonByName(nume)
    }

    suspend fun addPerson(person: Model_Person)
    {
        personDao.addPerson(person)
    }

}