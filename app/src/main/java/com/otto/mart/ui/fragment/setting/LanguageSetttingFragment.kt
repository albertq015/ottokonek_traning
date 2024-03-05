package com.otto.mart.ui.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import com.otto.mart.model.localmodel.setting.LanguageOption
import com.otto.mart.ui.Partials.adapter.setting.LanguageOptionAdapter
import com.otto.mart.ui.activity.settings.SettingActivity
import kotlinx.android.synthetic.main.fragment_language_setting.*

/**O
 * A simple [Fragment] subclass.
 */
class LanguageSetttingFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_language_setting, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerview()
        displayLanguageOption()
    }

    private fun initView() {
        ivClose.setOnClickListener {
            (activity as SettingActivity).hideLanguageSettingFragment()
        }
    }

    private fun initRecyclerview() {
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
    }

    private fun displayLanguageOption() {
        val languageList = mutableListOf<LanguageOption>()

        val english = LanguageOption()
        english.name = "English"
        english.code = "en"
        languageList.add(english)

        val indonesia = LanguageOption()
        indonesia.name = "Indonesia"
        indonesia.code = "in"
        languageList.add(indonesia)

        activity?.let {
            val adapter = LanguageOptionAdapter(it, languageList)
            recyclerview.adapter = adapter

            adapter.setmOnViewClickListener(object : LanguageOptionAdapter.OnViewClickListener {
                override fun onViewClickListener(data: LanguageOption, position: Int) {
                    languageSelected(data, position)
                }
            })
        }
    }

    private fun languageSelected(data: LanguageOption, position: Int) {
        data.code?.let {
            (activity as SettingActivity).setLanguageSetting(it)
        }
        ivClose.performClick()
    }
}