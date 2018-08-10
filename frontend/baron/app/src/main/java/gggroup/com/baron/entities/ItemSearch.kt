package gggroup.com.baron.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemSearch (
        var city: String? = "",
        var district: String? = "",
        var minPrice: Float? = 0F,
        var maxPrice: Float? = 0F,
        var type_house: Int? = 0
): Parcelable