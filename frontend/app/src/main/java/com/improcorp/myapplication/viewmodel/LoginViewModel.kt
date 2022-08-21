package com.improcorp.myapplication.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.improcorp.myapplication.model.*
import com.improcorp.myapplication.tool.MyRepository
import com.improcorp.myapplication.tool.MyRequest
import org.json.JSONArray

class LoginViewModel: BaseViewModel(){

    private lateinit var mRepository: MyRepository

    var goToHome = MutableLiveData<Boolean>().apply { value = false }
    var goToRegistration = MutableLiveData<Boolean>().apply { value = false }

    val inputPseudo = ObservableField<String>()
    val inputPassword = ObservableField<String>()
    val inputRememberMe = ObservableField<Boolean>()

    var isErrorLogin = ObservableField<Boolean>(false)

    var isLoginSaved = MutableLiveData<Boolean>().apply { value = false }

    fun setRepository(repo: MyRepository){ this.mRepository = repo }

    fun onRegisterClicked(){ goToRegistration.value = true }

    fun initializeList(){
        dataLoading.value = true

        mRepository.getInitializationList(object: MyRequest.InitializationCallback{
            override fun onSuccess(initArray: JSONArray) {
                val cardTemplateHashMap = LinkedHashMap<Int, String>()
                for (i in 0 until initArray.length()) {
                    val item = initArray.getJSONObject(i)

                    cardTemplateHashMap[item.getInt("ct_id")] = item.getString("ct_libelle")
                }
                InitialHashMap.templateHashMap = cardTemplateHashMap
            }

            override fun onError(message: String) {
                toastMessage.value= message
            }

            override fun inputErrors(errorHashMap: HashMap<String, String>) {
                for(item in errorHashMap){
                    toastMessage.value= item.value
                }
            }

        })
    }

    fun onLoginClicked(){
        dataLoading.value = true

        isErrorLogin.set(inputPseudo.get().isNullOrEmpty() || inputPassword.get().isNullOrEmpty())

        if(isErrorLogin.get() == false){
            User.mPseudo = inputPseudo.get()
            User.mPassword = inputPassword.get()

            mRepository.login(object: MyRequest.LoginCallback{
                override fun onSuccess(message: String) {
                    goToHome.value = true

                    toastMessage.value= message
                }

                override fun onError(message: String) {
                    toastMessage.value= message
                }

                override fun inputErrors(errorHashMap: HashMap<String, String>) {
                    for(item in errorHashMap){
                        when(item.key){
                            "pseudo", "password" -> isErrorLogin.set(true)
                            else -> toastMessage.value= item.value
                        }
                    }
                }
            })
        }
    }
}