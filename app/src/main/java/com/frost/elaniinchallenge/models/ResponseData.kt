package com.frost.elaniinchallenge.models

import com.frost.elaniinchallenge.Region

data class ResponseData(
    val errorMessage: String?=null,
    val regionList: ArrayList<Region>?= arrayListOf()
)
