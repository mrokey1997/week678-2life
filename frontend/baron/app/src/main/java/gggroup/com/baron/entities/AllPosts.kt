package gggroup.com.baron.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllPosts(
        var posts: OverviewPosts?
):Parcelable