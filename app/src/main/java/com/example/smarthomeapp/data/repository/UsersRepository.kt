package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.data.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val usersCollection = firestore.collection("users")
    suspend fun getUsers(): List<Users> {
        try {
            val querySnapshot = usersCollection.get().await()
            val usersList = mutableListOf<Users>()
            for (document in querySnapshot) {
                val user = document.toObject(Users::class.java)
                if (user.role != UserRole.ADMIN_ROLE.name) {
                    usersList.add(user)
                }
            }
            return usersList
        } catch (e: Exception) {
            // Handle any errors or exceptions here
            throw e
        }
    }

    suspend fun getUser(userId: String): Users? {
        val userDocument = usersCollection.document(userId)
        val documentSnapshot = userDocument.get().await()
        return if (documentSnapshot.exists()) {
            documentSnapshot.toObject(Users::class.java)
        } else {
            null
        }
    }

    suspend fun deleteUser(userId: String): String {
        val userDocument = usersCollection.document(userId)
        return try {
            userDocument.delete().await()
            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    suspend fun saveUser(user: Users): String {
        return try {
            // Firestore-specific logic here
            val userDocRef = usersCollection.document(user.id)
            userDocRef.set(user).await()
            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    suspend fun updateUserFields(
        userId: String,
        updatedFields: Map<String, Any>,
        password: String,
        oldPassword: String = "",
        email: String = ""
    ): String {
        val userDocument = usersCollection.document(userId)

        val result = runCatching {
            userDocument.update(updatedFields).await()
            if (password.isNotEmpty() && oldPassword.isNotEmpty() && email.isNotEmpty()) {
                runCatching {
                    firebaseAuth.signInWithEmailAndPassword(email, oldPassword).await()
                }.onFailure {
                    return it.message.toString()
                }
                runCatching {
                    firebaseAuth.currentUser?.updatePassword(password)?.await()
                }.onFailure {
                    return it.message.toString()
                }
            }
            "Done"
        }.onFailure {
            return it.message.toString()
        }

        return result.getOrThrow()
    }

    suspend fun getUserRole(): String? {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            return try {
                val userDocument = usersCollection.document(uid)
                val documentSnapshot = userDocument.get().await()
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(Users::class.java)
                    Log.d("getUserRole", userData.toString())
                    userData?.role
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        // Return null if the user is not logged in or any error occurs
        return null
    }

}
