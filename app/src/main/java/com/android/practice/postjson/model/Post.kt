package com.android.practice.postjson.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "post_table")
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
) : Parcelable