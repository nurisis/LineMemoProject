package com.hinuri.linememoproject.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true) var id:Int = 0,
    var title:String = "",
    var content:String = "",
    var imageArr:MutableList<String> = mutableListOf(),
    var createdTime:String = "",
    var updatedTime:String = ""
) : Serializable