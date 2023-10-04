package com.example.play4free

import android.app.Application
import android.net.Uri
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storage = Firebase.storage.reference
    private var database = getData(application)
    private val repo = AppRepository(GamesApi, GiveawayApi, database)
    val gameList = repo.gameList
    val giveawayList = repo.giveawayList
    val listOfId = mutableListOf<Long>()


    private val _gameDetail: MutableLiveData<GameDetail> = MutableLiveData()
    val gameDetail: LiveData<GameDetail>
        get() = _gameDetail

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user

    private var _currentUserProfile: MutableLiveData<Profile> = MutableLiveData()
    val currentUserProfile: LiveData<Profile>
        get() = _currentUserProfile

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

    fun updateFav(like: Boolean, id: Long){
        viewModelScope.launch (Dispatchers.IO){
            repo.updateFav(like,id)
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

        if (_user.value != null){
            profileRef.get().addOnSuccessListener {
                _currentUserProfile.value = it.toObject(Profile::class.java)
            }
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
        if (listOfId != null){
            for (item in listOfId){
                updateFav(false, item)
            }
        }
        firebaseAuth.signOut()
        listOfId.clear()
        _user.value = firebaseAuth.currentUser
    }

    fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                setupUserEnv()
                firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                    var profile = it.toObject(Profile::class.java)
                    if (profile?.favList != null){
                        for (item in profile!!.favList){
                            addLikedItem(item)
                            updateFav(true, item)
                        }
                    }
                }
            } else {
                Toast.makeText(getApplication(), "Wrong email or password!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun resetPw(){
        firebaseAuth.sendPasswordResetEmail(_user.value?.email!!)
    }

    fun addLikedItem(id: Long){
        listOfId.add(id)
        firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid).update("favList", listOfId)
    }

    fun removeLikedItem(id: Long){
        listOfId.remove(id)
        firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid).update("favList", listOfId)
    }

    fun uploadImage(uri: Uri){
        try {
            val postId = generateImageId()
            val imageFileName = "$postId.jpg"
            val imageReference = storage.child("images/${user.value?.uid}/$imageFileName")
            val imageRef = imageReference.child(imageFileName)
            val uploadTask = imageRef.putFile(uri)

            uploadTask.addOnCompleteListener {
                if (it.isSuccessful){
                    imageRef.downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful){
                            val imageUrl = it.result.toString()
                            firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid).update("pb", imageUrl)
                        }
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateImageId(): String {
        return System.currentTimeMillis().toString()
    }
}