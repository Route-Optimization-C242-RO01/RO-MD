package com.example.myrouteoptimization.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RouteResponse(

	@field:SerializedName("data")
	val data: List<DataItem> = emptyList(),

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("code")
	val code: Int? = null
)

data class DataItem(

	@field:SerializedName("data_route_results")
	val dataRouteResults: List<DataRouteResultsItem?>? = null,

	@field:SerializedName("id_results")
	val idResults: String? = null,

	@field:SerializedName("total_distance")
	val totalDistance: Float? = null,

	@field:SerializedName("number_of_vehicles")
	val numberOfVehicles: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataDetailRouteRouteItem(

	@field:SerializedName("id_route")
	val idRoute: String? = null,

	@field:SerializedName("demand")
	val demand: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("id_detail_route")
	val idDetailRoute: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("postal_code")
	val postalCode: String? = null,

	@field:SerializedName("sequence")
	val sequence: Int? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)

data class DataRouteResultsItem(

	@field:SerializedName("id_route")
	val idRoute: String? = null,

	@field:SerializedName("id_results")
	val idResults: String? = null,

	@field:SerializedName("vehicle_sequence")
	val vehicleSequence: Int? = null,

	@field:SerializedName("data_detailRoute_route")
	val dataDetailRouteRoute: List<DataDetailRouteRouteItem?>? = null
)