package com.example.pra06_sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLite를 사용하기 위한 클래스
class PostDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // 데이터베이스가 최초 생성될때
    // 앱이 새로 시작되거나 삭제후 재설치 될때
    override fun onCreate(db: SQLiteDatabase?) {
        val createSql = CREATE_SQL_VER3
        db?.execSQL(createSql)
    }

    // 데이터베이스 버전이 오른경우
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        when(oldVersion) {
            1 -> db?.execSQL("ALTER TABLE post ADD COLUMN post TEXT")
            in 1..2 -> db?.execSQL("ALTER TABLE post ADD COLUMN time TEXT")
        }
    }

    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "post.db"

        // 데이터 생성 쿼리를 버전별로 가지고 있다. 현재 버전은 3
        const val CREATE_SQL_VER1 = "CREATE TABLE post(" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT )"
        const val CREATE_SQL_VER2 = "CREATE TABLE post(" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT," +
                "post TEXT )"
        const val CREATE_SQL_VER3 = "CREATE TABLE post(" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT," +
                "post TEXT," +
                "time TEXT )"
    }

}