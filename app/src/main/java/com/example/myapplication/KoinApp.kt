package com.example.myapplication

import android.app.Application
import androidx.room.Room
import com.example.myapplication.Database.TodoDatabase
import com.example.myapplication.reposetories.TodoRepo
import com.example.myapplication.reposetories.TodoRepoImpl
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

class KoinApp:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(module {

                single {
                    Room
                        .databaseBuilder(this@KoinApp,TodoDatabase::class.java,"db")
                        .build()
                }
                single {
                    TodoRepoImpl(database = get())
                }bind TodoRepo::class
            })
        }
    }
}