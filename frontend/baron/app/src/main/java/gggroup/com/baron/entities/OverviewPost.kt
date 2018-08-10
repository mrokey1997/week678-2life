package gggroup.com.baron.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OverviewPost (
        var id: Int?,
        var title: String?,
        var price: Float?,
        var area: Int?,
        var description: String?,
        var date_post: String?,
        var phone_contact_number: String?,
        var type_house: Int?,
        var sex: Int?,
        var detail_ids: List<String>?,
        var image: ImageOverviewPost?,
        var address: AddressPost?
):Parcelable