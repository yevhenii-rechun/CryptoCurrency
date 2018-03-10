package com.zetokz.data.repository

import io.reactivex.Single

/**
 * Created by Yevhenii Rechun on 1/18/18.
 * Copyright © 2017. All rights reserved.
 */
interface HostHealthRepository {

    fun isAlive(): Single<Boolean>
}