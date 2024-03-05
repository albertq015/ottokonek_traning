package com.otto.mart.ui.activity.ppob

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.FavoriteItemModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.FavoriteResponseModel
import com.otto.mart.presenter.dao.PpobDao
import com.otto.mart.ui.Partials.adapter.ppob.PpobFavoriteAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup.Companion.KEY_FAVORITE_TOP_UP
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup.Companion.KEY_FAVORITE_VOUCHER_GAME
import com.otto.mart.ui.component.dialog.DeleteConfirmationFaveDialog
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.IConfimation
import kotlinx.android.synthetic.main.content_ppob_favorite_list.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class PpobFavoriteListActivity : AppActivity() ,IConfimation {
    override fun onClick() {
        deleteFavorite(favorite_id)
    }

    companion object {
        val KEY_PRODUCT_CODE = "product_code"
    }

    var KEY_API_DELETE_FAVORITE = 100

    var mPpobFavoriteCode: String = ""
    var mProductCode: String = ""
    var favorite_id :Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_favorite_list)

        if (intent.hasExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_CODE)) {
            mPpobFavoriteCode = intent?.getStringExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_CODE)!!
        }

        if (intent.hasExtra(KEY_PRODUCT_CODE)) {
            mProductCode = intent?.getStringExtra(KEY_PRODUCT_CODE)!!
        }

        initView()
        initRecyclerView()
        getFavoriteList(false)
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_ppob_favorite_list)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayFavoriteList(ppobFavourites: MutableList<FavoriteItemModel>) {
        var favoriteList = mutableListOf<FavoriteItemModel>()

        if (!mProductCode.equals("")) {
            for (ppobFavourite in ppobFavourites) {
                if (mPpobFavoriteCode.equals(KEY_FAVORITE_TOP_UP, true)) {
                    if (ppobFavourite.wallet_code.equals(mProductCode)) {
                        favoriteList.add(ppobFavourite)
                    }
                }
//                else if (mPpobFavoriteCode.equals(KEY_FAVORITE_VOUCHER_GAME, true)) {
//                    if (ppobFavourite.game_code.equals(mProductCode)) {
//                        favoriteList.add(ppobFavourite)
//                    }
//                }
                else {
                    if (ppobFavourite.product_code.equals(mProductCode)) {
                        favoriteList.add(ppobFavourite)
                    }
                }
            }
        } else {
            favoriteList = ppobFavourites
        }

        if(favoriteList.size > 0) {

            val adapter = PpobFavoriteAdapter(this, favoriteList)
            recyclerview.adapter = adapter

            adapter.setmOnViewClickListener(object: PpobFavoriteAdapter.OnViewClickListener {
                override fun onViewClickListener(favorite: FavoriteItemModel, position: Int) {
                    favoriteSelected(favorite)
                }

                override fun onDeleteListener(favorite: FavoriteItemModel) {
                    favorite_id = favorite.id
                    var delete = DeleteConfirmationFaveDialog(this@PpobFavoriteListActivity,this@PpobFavoriteListActivity,favorite.customer_reference,this@PpobFavoriteListActivity)
                    delete.show()
                }
            })
        } else {
            recyclerview.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        }
        viewAnimator.displayedChild = 1
    }

    private fun favoriteSelected(favoriteItemModel: FavoriteItemModel) {
        val intent = Intent()
        intent.putExtra(AppKeys.KEY_PPOB_FAVORITE_DATA, Gson().toJson(favoriteItemModel))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    //region API Call

    private fun getFavoriteList(isAfterDelete: Boolean) {
        if (isAfterDelete) {
            ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()
        }

        PpobDao(this).favoriteList(mPpobFavoriteCode, BaseDao.getInstance(this, AppKeys.API_KEY_PPOB_FAVORITE).callback)
    }

    private fun deleteFavorite(id: Int) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        PpobDao(this).deleteFavorite(id, BaseDao.getInstance(this, KEY_API_DELETE_FAVORITE).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiResponseCallback(br, responseCode, response)
        if (response != null) {
            if (response.isSuccessful) {
                if (responseCode == AppKeys.API_KEY_PPOB_FAVORITE) {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        displayFavoriteList((br as FavoriteResponseModel).ppob_favourites)
                    } else {
                        recyclerview.visibility = View.GONE
                        emptyLayout.visibility = View.VISIBLE
                        viewAnimator.displayedChild = 1
                    }
                } else if (responseCode == KEY_API_DELETE_FAVORITE) {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        getFavoriteList(true)
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        super.onApiFailureCallback(message, ac)
        val error=ErrorDialog(this,this,false,false,message,message)
        error.show()

    }

    override fun onApiResponseError() {
        onApiResponseError()
    }

    //endregion API Call
}
