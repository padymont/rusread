package com.padym.rusread.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syllables")
data class Syllable(
    @PrimaryKey val key: String,
    val resId: String
) {
    companion object {
        private val consonantGroups = listOf(
            listOf("б", "в", "г", "д", "з"),
            listOf("к", "л", "м", "н"),
            listOf("п", "р", "с", "т"),
            listOf("л", "м", "н", "р"),
            listOf("б", "п"),
            listOf("в", "ф"),
            listOf("г", "к"),
            listOf("д", "т"),
        )
        private val vowelGroups = listOf(
            listOf("а", "о", "у", "ы", "э"),
            listOf("е", "ё", "и", "ю", "я")
        )

        fun getFirstTimeGroup() = setOf("ма", "мо", "му", "па", "по", "пу")

        fun getPreselectedGroups() = buildNormalGroups() +
                getSingleLetterGroups() +
                getHissConsonantGroups() +
                getSoftSignGroups()

        private fun buildNormalGroups(): List<List<String>> {
            return vowelGroups.flatMap { vowels ->
                consonantGroups.map { consonants ->
                    consonants.flatMap { consonant ->
                        vowels.map { vowel -> consonant + vowel }
                    }
                }
            }
        }

        private fun getSingleLetterGroups() = listOf(
            listOf("а", "е", "ё", "и", "о", "у", "ы", "э", "ю", "я"),
            listOf("б", "в", "г", "д", "ж", "з", "й", "к", "л", "м", "н"),
            listOf("п", "р", "с", "т", "ф", "х", "ц", "ч", "ш", "щ")
        )

        private fun getHissConsonantGroups() = listOf(
            listOf(
                "жа", "жо", "жу", "хэ",
                "ха", "хо", "ху", "хы", "хэ",
                "ца", "цо", "цу", "цы", "цэ",
                "ча", "чо", "чу",
                "ша", "шо", "шу",
                "ща", "що", "щу"
            ),
            listOf(
                "же", "жё", "жи",
                "хе", "хё", "хи", "хю", "хя",
                "це", "ци",
                "че", "чё", "чи",
                "ше", "шё", "ши",
                "ще", "щё", "щи",
            )
        )

        private fun getSoftSignGroups() = listOf(
            listOf("б", "в", "г", "д", "бь", "вь", "гь", "дь"),
            listOf("к", "л", "м", "н", "кь", "ль", "мь", "нь"),
            listOf("п", "р", "с", "т", "пь", "рь", "сь", "ть"),
            listOf("з", "зь", "ж", "жь", "ч", "чь", "ш", "шь", "щ", "щь"),
        )
    }
}