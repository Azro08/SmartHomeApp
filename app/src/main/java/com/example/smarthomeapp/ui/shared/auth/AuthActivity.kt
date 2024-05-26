package com.example.smarthomeapp.ui.shared.auth

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.ActivityAuthBinding
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.setLocale
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(R.layout.activity_auth) {
    private val binding by viewBinding(ActivityAuthBinding::bind)

    override fun attachBaseContext(newBase: Context?) {
        val lang = newBase?.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            ?.getString(Constants.LANGUAGE_KEY, "en")!!.toString()
        super.attachBaseContext(ContextWrapper(newBase.setLocale(lang)))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setViewPager()
    }

    private fun setViewPager() {

        val adapter = AuthViewPagerAdapter(this)
        binding.authViewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.authViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.login)
                1 -> getString(R.string.register)
                else -> ""
            }
        }.attach()

    }
}