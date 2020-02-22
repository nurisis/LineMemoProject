package com.hinuri.linememoproject.data.util

import androidx.room.TypeConverter

class RoomConverters {

    @TypeConverter
    fun imageArrToString(imageArr:List<String>):String {
        var imageString = ""

        imageArr.forEach { "${it}@" }

        return imageString
    }

    @TypeConverter
    fun imageStringToArray(imageString:String) : List<String> {
        return imageString.split("@")
    }

}