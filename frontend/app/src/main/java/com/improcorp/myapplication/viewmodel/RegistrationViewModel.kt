package com.improcorp.myapplication.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.improcorp.myapplication.model.User
import com.improcorp.myapplication.tool.MyRepository
import com.improcorp.myapplication.tool.MyRequest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RegistrationViewModel: BaseViewModel() {

    private lateinit var mRepository: MyRepository

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    var goToLogin = MutableLiveData<Boolean>().apply { value = false }

    val inputLastName = ObservableField<String>()
    val inputFirstName = ObservableField<String>()
    val inputEmailAddress = ObservableField<String>()
    val inputPseudo = ObservableField<String>()
    val inputPassword1 = ObservableField<String>()
    val inputPassword2 = ObservableField<String>()

    var isErrorLastName = ObservableField<Boolean>(false)
    var isErrorFirstName = ObservableField<Boolean>(false)
    var isErrorEmailAddress = ObservableField<Boolean>(false)
    var isErrorPseudo = ObservableField<Boolean>(false)
    var isErrorPassword1 = ObservableField<Boolean>(false)
    var isErrorPassword2 = ObservableField<Boolean>(false)

    fun setRepository(repo: MyRepository){ this.mRepository = repo }

    private fun resetInput(){
        inputLastName.set(null)
        inputFirstName.set(null)
        inputEmailAddress.set(null)
        inputPseudo.set(null)
        inputPassword1.set(null)
        inputPassword2.set(null)
    }

    fun onLoginClicked(){ goToLogin.value = true }

    fun onRegisterClicked(){
        dataLoading.value = true

        isErrorLastName.set(inputLastName.get().isNullOrEmpty())
        isErrorFirstName.set(inputFirstName.get().isNullOrEmpty())
        isErrorEmailAddress.set(inputEmailAddress.get().isNullOrEmpty())
        isErrorPseudo.set(inputPseudo.get().isNullOrEmpty())
        isErrorPassword1.set(inputPassword1.get().isNullOrEmpty())
        isErrorPassword2.set(inputPassword2.get().isNullOrEmpty())

        val confirmationPassword = inputPassword2.get()

        if(isErrorLastName.get() == false
            && isErrorFirstName.get() == false
            && isErrorEmailAddress.get() == false
            && isErrorPseudo.get() == false
            && isErrorPassword1.get() == false
            && isErrorPassword2.get() == false
            && confirmationPassword != null){

            User.mLastName = inputLastName.get()
            User.mFirstName = inputFirstName.get()
            User.mEmailAddress = inputEmailAddress.get()
            User.mPseudo = inputPseudo.get()
            User.mPassword = inputPassword1.get()
            User.mRegistrationDate = sdf.format(Date())

            mRepository.register(confirmationPassword, object: MyRequest.RegisterCallback{
                override fun onSuccess(message: String) {
                    goToLogin.value = true

                    toastMessage.value= message
                }

                override fun onError(message: String) {
                    resetInput()
                    toastMessage.value= message
                }

                override fun inputErrors(errorHashMap: HashMap<String, String>) {
                    for(item in errorHashMap){
                        when(item.key){
                            "last_name" -> {isErrorLastName.set(true); inputLastName.set(null)}
                            "first_name" -> {isErrorFirstName.set(true); inputFirstName.set(null)}
                            "email_address" -> {isErrorEmailAddress.set(true); inputEmailAddress.set(null)}
                            "pseudo" -> {isErrorPseudo.set(true); inputPseudo.set(null)}
                            "password" -> {isErrorPassword2.set(true); inputPassword1.set(null); inputPassword2.set(null)}
                            else -> {resetInput(); toastMessage.value= item.value}
                        }
                    }
                }
            })
        }
    }
}