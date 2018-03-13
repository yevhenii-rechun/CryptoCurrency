package com.zetokz.cryptocurrencyrates.util.rx

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject

/**
 * Created by Yevhenii Rechun on 3/13/18.
 * Copyright © 2017. All rights reserved.
 */
class ClickSubjectDelegate : Observable<Boolean>() {

    private val clickPublisher = PublishSubject.create<Boolean>()

    fun click() {
        clickPublisher.onNext(true)
    }

    override fun subscribeActual(observer: Observer<in Boolean>?) {
        clickPublisher.subscribeActual(observer)
    }
}