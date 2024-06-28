package com.example.myapplication.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date

@Entity("todos")
data class TodoEntity(

    @PrimaryKey(autoGenerate = true)
      val iD: Int=0,

    @ColumnInfo("Tittle")
    val tittle:String,

    @ColumnInfo("Sub_Tittle")
    val subTittle:String,

    @ColumnInfo("done")
    val done:Boolean=false,

    @ColumnInfo("added")
    val added:Long=System.currentTimeMillis()


)

val TodoEntity.addDate:String get()=SimpleDateFormat("yyyy/MM/dd hh:mm ").format(Date(added))


