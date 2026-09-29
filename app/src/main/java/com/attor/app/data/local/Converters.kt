package com.attor.app.data.local

import androidx.room.TypeConverter
import com.attor.app.data.WorkFormat

class Converters {
    @TypeConverter
    fun fromWorkFormat(format: WorkFormat): String = format.name

    @TypeConverter
    fun toWorkFormat(value: String): WorkFormat = try {
        WorkFormat.valueOf(value)
    } catch (e: Exception) {
        WorkFormat.NOVEL
    }
}
