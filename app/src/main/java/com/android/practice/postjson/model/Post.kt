package com.android.practice.postjson.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "post_table")
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
) : Parcelable