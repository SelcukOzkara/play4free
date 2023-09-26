package com.example.play4free

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.play4free.data.AppRepository
import com.example.play4free.data.datamodels.GameDetail
import com.example.play4free.data.datamodels.Profile
import com.example.play4free.data.local.getData
import com.example.play4free.data.remote.GamesApi
import com.example.play4free.data.remote.GiveawayApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    private var database = getData(application)
    private val repo = AppRepository(GamesApi, GiveawayApi, database)
    val gameList = repo.gameList
    val giveawayList = repo.giveawayList


    private val _gameDetail: MutableLiveData<GameDetail> = MutableLiveData()
    val gameDetail: LiveData<GameDetail>
        get() = _gameDetail

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user

    private val _status: MutableLiveData<Int> = MutableLiveData()
    val status: LiveData<Int>
        get() = _status

    lateinit var profileRef: DocumentReference

    init {
        setupUserEnv()
    }


    fun loadGameList() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getGameList()
        }
    }

    fun getGameDetail(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _gameDetail.postValue(repo.getGameDetail(id)!!)
        }
    }

    fun loadGiveawayList() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getGiveaway()
        }
    }

    fun setupUserEnv() {
        _user.value = firebaseAuth.currentUser
        firebaseAuth.currentUser?.let {
            profileRef = firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
        }
    }

    fun signUp(email: String, password: String, username: String, date: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                setupUserEnv()
                val profile = Profile(username = username, date = date)
                profileRef.set(profile)
                _status.value = 1
            } else {
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthUserCollisionException) {
                    _status.value = 2
                }
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _user.value = firebaseAuth.currentUser
    }

    fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                setupUserEnv()
            } else {
                Toast.makeText(getApplication(), "Wrong email or password!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}