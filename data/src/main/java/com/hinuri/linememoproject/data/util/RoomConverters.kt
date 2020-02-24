package com.hinuri.linememoproject.data.util

/**
 * Room에 원시 타입 외의 값이 저장되어야 할 때 DB에 저장될 수 있도록 값을 변환해줌.
 */

import androidx.room.TypeConverter
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
            imageString?.split("@").forEach {
                if(it.isNotEmpty()) add(it)
            }
        }
    }

}