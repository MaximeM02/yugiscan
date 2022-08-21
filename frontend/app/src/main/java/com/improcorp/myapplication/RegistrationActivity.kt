package com.improcorp.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.improcorp.myapplication.databinding.ActivityRegistrationBinding
import com.improcorp.myapplication.ui.RegistrationFragment

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)

        if (savedInstanceState == null) {
            val fragment: Fragment = RegistrationFragment()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_registration, fragment)
            transaction.commit()
        }
    }
}