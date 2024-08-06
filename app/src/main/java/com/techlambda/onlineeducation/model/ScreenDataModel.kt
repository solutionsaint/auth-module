package com.techlambda.onlineeducation.model

data class ScreenDataModel(

	val result: Result? = null,

	val message: String? = null,

	val statusCode: Int? = null
)

data class Result(

	val createdAt: String? = null,

	val discription: String? = null,

	val title: String? = null,

	val key: String? = null,

	val updatedAt: String? = null
)
