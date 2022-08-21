package com.improcorp.myapplication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.improcorp.myapplication.LoginActivity
import com.improcorp.myapplication.databinding.FragmentRegistrationBinding
import com.improcorp.myapplication.tool.MyRepository
import com.improcorp.myapplication.viewmodel.RegistrationViewModel

class RegistrationFragment: Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var registrationViewModel: RegistrationViewModel

    private lateinit var mContext: FragmentActivity
    private lateinit var repo: MyRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        repo = MyRepository(mContext)

        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        registrationViewModel.setRepository(repo)

        binding.registrationViewModel = registrationViewModel

        setupObservable()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as FragmentActivity
    }

    private fun setupObservable(){
        registrationViewModel.goToLogin.observe(mContext, Observer {
            if(it){
                val intent = Intent(mContext, LoginActivity::class.java)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                mContext.finish()
            }
        })

        registrationViewModel.toastMessage.observe(mContext, Observer {
            Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
        })
    }
}