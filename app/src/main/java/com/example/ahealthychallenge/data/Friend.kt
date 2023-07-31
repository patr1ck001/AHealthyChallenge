package com.example.ahealthychallenge.data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import de.hdodenhof.circleimageview.CircleImageView

data class Friend(
    var firstName: String? = null,
    var lastName: String? = null,
    var bitmap: Bitmap? = null,
    var username: String? = null,
    var currentUsername: String? = null,
    val pointsSheet: UserPointsSheet? = null,
)
