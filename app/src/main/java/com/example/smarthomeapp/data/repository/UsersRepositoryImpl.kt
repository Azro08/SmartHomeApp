package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.model.UserDto
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.domain.repository.UsersRepository
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : UsersRepository {

    private val usersCollection = firestore.collection("users")
    suspend fun getUsers(): List<UserDto> {
        try {
            val querySnapshot = usersCollection.get().await()
            val usersList = mutableListOf<UserDto>()
            for (document in querySnapshot) {
                val user = document.toObject(UserDto::class.java)
                if (user.role == UserRole.USER_ROLE.name) {
                    usersList.add(user)
                }
            }
            return usersList
        } catch (e: Exception) {
            // Handle any errors or exceptions here
            throw e
        }
    }

    suspend fun getMasters(): List<UserDto> {
        try {
            val querySnapshot = usersCollection.get().await()
            val usersList = mutableListOf<UserDto>()
            for (document in querySnapshot) {
                val user = document.toObject(UserDto::class.java)
                if (user.role == UserRole.MASTER_ROLE.name) {
                    usersList.add(user)
                }
            }
            return usersList
        } catch (e: Exception) {
            // Handle any errors or exceptions here
            throw e
        }
    }

    override suspend fun deleteUsersProducts(uid: String) {
        val firestore = FirebaseFirestore.getInstance()
        val productsCollection = firestore.collection("products")

        val query = productsCollection.whereEqualTo("userId", uid)

        val querySnapshot: QuerySnapshot = query.get().await()

        val deletionTasks: MutableList<Task<Void>> = mutableListOf()
        for (document in querySnapshot.documents) {
            val deletionTask = productsCollection.document(document.id).delete()
            deletionTasks.add(deletionTask)
        }

        try {
            Tasks.await(Tasks.whenAll(deletionTasks))
        } catch (e: Exception) {
            Log.d("UsersRep", e.message.toString())
        }
    }


    suspend fun getUser(userId: String): UserDto? {
        Log.d("UserIdRep", userId)
        val userDocument = usersCollection.document(userId)
        val documentSnapshot = userDocument.get().await()
        return if (documentSnapshot.exists()) {
            documentSnapshot.toObject(UserDto::class.java)
        } else {
            null
        }
    }

    suspend fun getUserByFullNameAndId(fullName: String, id: String): UserDto? {
        val querySnapshot =
            usersCollection.whereEqualTo("fullName", fullName).whereEqualTo("id", id).get().await()
        if (querySnapshot.isEmpty) {
            return null
        }
        return querySnapshot.documents[0].toObject(UserDto::class.java)
    }

    override suspend fun deleteAccount(userId: String): String {
        val userDocument = usersCollection.document(userId)
        return try {
            userDocument.delete().await()
            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    suspend fun updateMasterBusyTimes(busyTimes: List<String>, masterId: String): String {
        return try {
            val masterDocument = usersCollection.document(masterId)
            masterDocument.update("busyTimes", busyTimes).await()
            "Done"
        } catch (e: FirebaseFirestoreException) {
            Log.d("UsersRep", e.message.toString())
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


}
