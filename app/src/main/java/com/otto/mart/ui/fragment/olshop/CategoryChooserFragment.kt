package com.otto.mart.ui.fragment.olshop

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.olshop.Category
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.CategoryChooserAdapter
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.toolbar_dialog.*

class CategoryChooserFragment : BottomSheetDialogFragment() {

    companion object {

        @JvmStatic
        fun newInstanse(categories: MutableList<Category>?, selectedListener: (Category) -> Unit): CategoryChooserFragment {
            return CategoryChooserFragment().apply {
                this.categories = categories
                this.selectedListener = selectedListener
            }
        }
    }

    private var behavior: BottomSheetBehavior<FrameLayout>? = null
    private var listener: (Int, Category) -> Unit = { index, selectedCategory ->
        run { showSelectedCategory(index, selectedCategory) }
    }

    private var categories: MutableList<Category>? = null
    private lateinit var selectedListener: (Category) -> Unit
    private var selectedCategories = mutableListOf<Category>()
    private val adapter = CategoryChooserAdapter(listener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            layoutInflater.inflate(R.layout.fragment_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvToolbarTitle.text = getString(R.string.text_category)
        categories?.let {
            adapter.categories = ArrayList(it)
        }

        val layoutParams = categoryList.layoutParams
        layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
        categoryList.layoutParams = layoutParams

        categoryList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        categoryList.setHasFixedSize(true)
        categoryList.adapter = adapter

        btnToolbarBack.setOnClickListener { dismiss() }
        backNavCategory.setOnClickListener {

            if (selectedCategories.size > 1) {
                val categories = ArrayList(selectedCategories[selectedCategories.lastIndex - 1].sub_categories)
                val category = Category(selectedCategories[selectedCategories.lastIndex - 1])
                if (!category.name.contains("semua", true)) {
                    category.name = "Semua ${category.name}"
                    category.sub_categories = mutableListOf()
                    categories.add(0, category)
                }

                adapter.categories = categories
                selectedCategories.removeAt(selectedCategories.lastIndex)
            } else {
                adapter.categories = categories?.let { it1 -> ArrayList(it1) }
                selectedCategories = mutableListOf()
                backNavCategory.gone()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        bottomSheet?.let {
            behavior = BottomSheetBehavior.from(bottomSheet)
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun showSelectedCategory(intex: Int, selectedCategory: Category) {
        if (selectedCategory.sub_categories.size > 0) {
            backNavCategory.visible()
            selectedCategories.add(selectedCategory)
            val categories = ArrayList(selectedCategory.sub_categories)
            val category = Category(selectedCategory)
            if (!category.name.contains("semua", true)) {
                category.name = "Semua ${category.name}"
                category.sub_categories = mutableListOf()
                categories.add(0, category)
            }

            adapter.categories = categories
        } else {
            selectedListener(selectedCategory)
            dismiss()
        }
    }
}