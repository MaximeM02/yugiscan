package com.improcorp.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.improcorp.myapplication.HomeActivity
import com.improcorp.myapplication.RegistrationActivity
import com.improcorp.myapplication.databinding.FragmentLoginBinding
import com.improcorp.myapplication.tool.MyRepository
import com.improcorp.myapplication.viewmodel.LoginViewModel

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var mContext: FragmentActivity
    private lateinit var repo: MyRepository

    private lateinit var loginPreferences: SharedPreferences
    private lateinit var loginPrefsEditor: SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        repo = MyRepository(mContext)

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.setRepository(repo)
        loginViewModel.initializeList()

        loginPreferences = mContext.getSharedPreferences("loginPrefs", AppCompatActivity.MODE_PRIVATE)

        loginViewModel.isLoginSaved.value = loginPreferences.getBoolean("saveLogin", false)
        println(loginPreferences.getBoolean("saveLogin", false))
        println(loginPreferences.getString("pseudo", ""))
        binding.loginViewModel = loginViewModel

        setupObservable()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as FragmentActivity
    }

    private fun setupObservable(){
        loginViewModel.goToHome.observe(mContext, Observer {
            if(it){
                loginPrefsEditor = loginPreferences.edit()
                loginPrefsEditor.putBoolean("saveLogin", loginViewModel.inputRememberMe.get()!!)

                if(loginViewModel.inputRememberMe.get()!!){
                    loginPrefsEditor.putString("pseudo", loginViewModel.inputPseudo.get()!!)
                    loginPrefsEditor.apply()
                } else{
                    loginPrefsEditor.clear()
                }
                loginPrefsEditor.commit()

                val intent = Intent(mContext, HomeActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                mContext.finish()
            }
        })

        loginViewModel.goToRegistration.observe(mContext, Observer {
            if(it){
                val intent = Intent(mContext, RegistrationActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                mContext.finish()
            }
        })

        loginViewModel.isLoginSaved.observe(mContext, Observer {
            loginViewModel.inputPseudo.set(loginPreferences.getString("pseudo", ""))
            loginViewModel.inputRememberMe.set(it)
        })

        loginViewModel.toastMessage.observe(mContext, Observer {
            Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
        })
    }
}