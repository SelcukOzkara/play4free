package com.example.play4free

import android.app.Application
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application): AndroidViewModel(application) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    private var database = getData(application)
    private val repo = AppRepository(GamesApi,GiveawayApi,database)
    val gameList = repo.gameList
    val giveawayList = repo.giveawayList

    private val _gameDetail: MutableLiveData<GameDetail> = MutableLiveData()
    val gameDetail: LiveData<GameDetail>
        get() = _gameDetail

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user

    lateinit var profileRef: DocumentReference

    init {
        setupUserEnv()
    }


    fun loadGameList(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getGameList()
        }
    }

    fun getGameDetail(id: Long) {
        viewModelScope.launch (Dispatchers.IO) {
            _gameDetail.postValue(repo.getGameDetail(id)!!)
        }
    }

    fun loadGiveawayList(){
        viewModelScope.launch (Dispatchers.IO) {
            repo.getGiveaway()
        }
    }

    fun setupUserEnv(){
        _user.value = firebaseAuth.currentUser
        firebaseAuth.currentUser?.let {
            profileRef = firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
        }
    }

    fun signUp(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            setupUserEnv()
            val profile = Profile()
            profileRef.set(profile)
        }
    }

    fun signOut(){
        firebaseAuth.signOut()
        _user.value = firebaseAuth.currentUser
    }

    fun signIn(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            setupUserEnv()
        }
    }
}