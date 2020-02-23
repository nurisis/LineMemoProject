package com.hinuri.linememoproject.data.util

import android.util.Log
import androidx.room.TypeConverter
import java.util.*
import kotlin.collections.ArrayList

class RoomConverters {

    @TypeConverter
    fun imageArrToString(imageArr:MutableList<String>?):String? {
        imageArr?.apply {
            var imageString = ""
            imageArr.forEach {
                imageString += "${it}@"
            }
            imageString = imageString.removeSuffix("@")

            return imageString
        }

        return null
    }

    @TypeConverter
    fun imageStringToArray(imageString:String?) : MutableList<String>? {
        if(imageString == null) return null

        return ArrayList<String>().apply {
            imageString?.split("@")?.forEach {
                add(it)
            }
        }
    }

}