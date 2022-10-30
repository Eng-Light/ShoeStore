package com.udacity.shoestore.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.User

class SharedViewModel : ViewModel() {
    val email = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()

    val password = MutableLiveData<String>()
    val passwordError = MutableLiveData<String?>()

    val valid = MutableLiveData<Boolean>()

    private val users = MutableLiveData<MutableList<User>>(mutableListOf())
    private val currentIndex = MutableLiveData(-1)

    val name = MutableLiveData<String>()
    val size = MutableLiveData<String>()
    val company = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    private val shoe = MutableLiveData<Shoe>()
    val validShoeData = MutableLiveData<Boolean>()
    val shoeError = MutableLiveData<String?>()

    //Validating data for New User
    fun validateNew() {
        var _valid = true
        if (email.value.isNullOrBlank()) {
            emailError.value = "Please Enter a Valid Email"
            _valid = false
        } else {
            emailError.value = null
        }
        if (password.value.isNullOrBlank()) {
            passwordError.value = "Please Enter a Valid Password"
            _valid = false
        } else {
            passwordError.value = null
        }
        if (_valid) {
            if (!checkUserExisting(email.value!!)) {
                addUser()
            } else {
                _valid = false
            }
        }
        valid.value = _valid
    }

    //Adding New User
    private fun addUser() {
        currentIndex.value = users.value?.size
        users.value?.add(User(email.value!!, mutableListOf()))
    }

    //Validating Input data for Existing user
    fun validateExisting() {
        var _valid = true
        if (email.value.isNullOrBlank()) {
            emailError.value = "Please Enter a Valid Email"
            _valid = false
        } else {
            emailError.value = null
        }
        if (password.value.isNullOrBlank()) {
            passwordError.value = "Please Enter a Valid Password"
            _valid = false
        } else {
            passwordError.value = null
        }
        if (_valid) {
            _valid = checkUserExisting(email.value!!)
        }
        valid.value = _valid
    }

    //Check if user already exist
    private fun checkUserExisting(_email: String): Boolean {
        var _valid = false
        for (user in users.value!!) {
            if (user.email == _email) {
                _valid = true
                //reset user index
                currentIndex.value = users.value!!.indexOf(user)
                break
            }
        }
        return _valid
    }

    //Reset *valid* to false
    fun onCompleteNavigation() {
        valid.value = false
    }

    //Adding New Show
    private fun addShoe(shoe: Shoe) {
        currentIndex.value?.let { users.value?.get(it)?.shoes?.add(shoe) }
    }

    //Validating InputField`s data
    fun validateShoeData() {
        var _valid = true

        //save new shoe`s value
        shoe.value = name.value?.let {
            size.value?.let { it1 ->
                description.value?.let { it2 ->
                    company.value?.let { it3 ->
                        Shoe(
                            it,
                            it1, it3, it2
                        )
                    }
                }
            }
        }
        if (shoe.value?.name.isNullOrBlank()) {
            shoeError.value = "Please Enter a Valid Data"
            _valid = false
        } else if (shoe.value?.size.isNullOrBlank()) {
            shoeError.value = "Please Enter a Valid Data"
            _valid = false
        } else if (shoe.value?.company.isNullOrBlank()) {
            shoeError.value = "Please Enter a Valid Data"
            _valid = false
        } else if (shoe.value?.description.isNullOrBlank()) {
            shoeError.value = "Please Enter a Valid Data"
            _valid = false
        } else {
            shoeError.value = null
        }
        if (_valid) {
            shoe.value?.let { addShoe(it) }
        }
        validShoeData.value = _valid
    }

    //reset valid to false
    fun onAddingComplete() {
        validShoeData.value = false
    }

    //get Shoe List in the fragment
    fun getShoeList(): MutableList<Shoe>? {
        if (shoe.value != null) {
            return currentIndex.value?.let { users.value?.get(it)!!.shoes }
        }
        return mutableListOf()
    }
}