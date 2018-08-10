package gggroup.com.baron.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressPost(
        var city : String?,
        var district: String?,
        var add_detail: String?
):Parcelable