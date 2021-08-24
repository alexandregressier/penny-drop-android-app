package dev.gressier.pennydrop.data

import android.text.TextUtils
import androidx.room.TypeConverter
import dev.gressier.pennydrop.data.GameState.UNKNOWN
import dev.gressier.pennydrop.game.AI
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter fun toOffsetDateTime(value: String?): OffsetDateTime? =
        value?.let { formatter.parse(it, OffsetDateTime::from) }

    @TypeConverter fun fromOffsetDateTime(date: OffsetDateTime?): String? =
        date?.format(formatter)


    @TypeConverter fun fromGameStateToInt(gameState: GameState?): Int =
        (gameState ?: UNKNOWN).ordinal

    @TypeConverter fun fromIntToGameState(gameStateInt: Int?): GameState =
        GameState.values().let { gameStateValues ->
            if (gameStateInt != null && gameStateValues.any { it.ordinal == gameStateInt })
                gameStateValues[gameStateInt]
            else UNKNOWN
        }


    @TypeConverter
    fun toIntList(value: String?) = value?.split(",")?.let { x -> x
            .filter { !TextUtils.isEmpty(it) }
            .map { it.toInt() }
    } ?: emptyList()

    @TypeConverter fun fromListOfIntToString(numbers: List<Int>?): String =
        numbers?.joinToString(",") ?: ""


    @TypeConverter fun toAI(aiId: Long?): AI? =
        AI.basicAI.firstOrNull { it.aiId == aiId }

    @TypeConverter fun fromAiToId(ai: AI?): Long? =
        ai?.aiId
}