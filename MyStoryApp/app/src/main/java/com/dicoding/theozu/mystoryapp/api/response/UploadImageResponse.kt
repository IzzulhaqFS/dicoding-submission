package com.dicoding.theozu.mystoryapp.api.response

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
