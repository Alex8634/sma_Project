package com.example.smamysuperalarm.repository

import com.example.smamysuperalarm.dao.UserDao
import com.example.smamysuperalarm.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUser(username: String, password: String): User? {
        return userDao.getUser(username, password)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updatePassword(username: String, newPassword: String) {
        userDao.updatePassword(username, newPassword)
    }

    suspend fun updateSleepHours(username: String, sleepHours: Int) {
        userDao.updateSleepHours(username, sleepHours)
    }
} 