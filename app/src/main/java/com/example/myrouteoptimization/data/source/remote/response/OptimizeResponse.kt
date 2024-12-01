package com.example.myrouteoptimization.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class OptimizeResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: OptimizeResponseData,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class OptimizeResponseData(

	@field:SerializedName("data_route_results")
	val dataRouteResults: List<DataRouteResultsItem>,

	@field:SerializedName("id_results")
	val idResults: String,

	@field:SerializedName("number_of_vehicles")
	val numberOfVehicles: Int,

	@field:SerializedName("total_distance")
	val totalDistance: Float,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
data class PostDataItem(

	@field:SerializedName("Postal_code")
	val postalCode: String,

	@field:SerializedName("Street")
	val street: String,

	@field:SerializedName("City")
	val city: String,

	@field:SerializedName("Kg")
	val kg: String = "0",

	@field:SerializedName("Province")
	val province: String
) : Parcelable

data class OptimizeRequest(
	@SerializedName("Number_of_vehicles") val numberOfVehicles : Int,
	@SerializedName("title") val title: String,
	@SerializedName("status") val status : String = "unfinished",
	@SerializedName("data") val data : List<PostDataItem>
)