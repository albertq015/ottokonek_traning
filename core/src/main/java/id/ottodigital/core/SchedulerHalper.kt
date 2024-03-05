package id.ottodigital.core

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
fun <T> Observable<T>.observableIOMain() =
        this.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


@SuppressLint("CheckResult")
fun <T> Single<T>.SingleIOMain() =
        this.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


@SuppressLint("CheckResult")
fun Completable.CompletableIOMain() =
        this.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
