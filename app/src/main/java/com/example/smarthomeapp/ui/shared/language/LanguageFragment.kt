package com.example.smarthomeapp.ui.shared.language

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smarthomeapp.MainActivity
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentLanguageBinding
import com.example.smarthomeapp.util.LocaleUtils
import com.example.smarthomeapp.util.setLocale
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : Fragment() {
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var langUtils: LocaleUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModelInputs()
        checkLanguage()
    }

    private fun checkLanguage() = with(binding) {
        val lang = resources.configuration.locales[0]

        if (lang.language == LANG_EN) {
            checkedEn.visibility = View.VISIBLE
            binding.lanEn.setBackgroundResource(R.drawable.bg_lan_selected)
        } else {
            checkedRus.visibility = View.VISIBLE
            lanRus.setBackgroundResource(R.drawable.bg_lan_selected)
        }
    }

    private fun bindViewModelInputs() = with(binding) {
        lanEn.setOnClickListener {
            checkedEn.visibility = View.VISIBLE
            checkedRus.visibility = View.GONE
            lanEn.setBackgroundResource(R.drawable.bg_lan_selected)
            lanRus.setBackgroundResource(R.drawable.bg_view_date)
            setLanguage(LANG_EN)
        }

        binding.lanRus.setOnClickListener {
            checkedRus.visibility = View.VISIBLE
            checkedEn.visibility = View.GONE
            lanRus.setBackgroundResource(R.drawable.bg_lan_selected)
            lanEn.setBackgroundResource(R.drawable.bg_view_date)
            setLanguage(LANG_RUS)
        }
    }


    private fun setLanguage(language: String) {
        requireContext().setLocale(language)
        langUtils.saveLang(language)
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val LANG_EN = "en"
        private const val LANG_RUS = "ru"
    }
}