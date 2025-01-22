package com.padym.rusread.viewmodels

data class Syllable(
    val key: String,
    val millisOffset: Int,
    val occurrenceCount: Int = 0
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

        fun findOffset(syllable: String): Int {
            return getAll().find { it.key == syllable }?.millisOffset ?: 0
        }

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

        fun getAll(): List<Syllable> = listOf(
            Syllable("ба", millisOffset = 660, occurrenceCount = 21),
            Syllable("бе", millisOffset = 2500, occurrenceCount = 29),
            Syllable("би", millisOffset = 6400, occurrenceCount = 27),
            Syllable("бо", millisOffset = 9200, occurrenceCount = 30),
            Syllable("бу", millisOffset = 10900, occurrenceCount = 53),
            Syllable("бы", millisOffset = 12900, occurrenceCount = 60),
            Syllable("ва", millisOffset = 22200, occurrenceCount = 81),
            Syllable("ве", millisOffset = 24200, occurrenceCount = 65),
            Syllable("ви", millisOffset = 27000, occurrenceCount = 37),
            Syllable("во", millisOffset = 29000, occurrenceCount = 82),
            Syllable("вы", millisOffset = 33000, occurrenceCount = 32),
            Syllable("га", millisOffset = 41500, occurrenceCount = 11),
            Syllable("ги", millisOffset = 46900, occurrenceCount = 12),
            Syllable("го", millisOffset = 50800, occurrenceCount = 111),
            Syllable("гу", millisOffset = 52300, occurrenceCount = 49),
            Syllable("да", millisOffset = 62800, occurrenceCount = 77),
            Syllable("де", millisOffset = 63800, occurrenceCount = 57),
            Syllable("ди", millisOffset = 68800, occurrenceCount = 34),
            Syllable("до", millisOffset = 70000, occurrenceCount = 52),
            Syllable("ду", millisOffset = 71100, occurrenceCount = 29),
            Syllable("дь", millisOffset = 76800, occurrenceCount = 20),
            Syllable("жа", millisOffset = 99, occurrenceCount = 17),
            Syllable("же", millisOffset = 99, occurrenceCount = 45),
            Syllable("жи", millisOffset = 99, occurrenceCount = 17),
            Syllable("за", millisOffset = 99, occurrenceCount = 69),
            Syllable("зо", millisOffset = 99, occurrenceCount = 12),
            Syllable("зы", millisOffset = 99, occurrenceCount = 27),
            Syllable("ка", millisOffset = 99, occurrenceCount = 180),
            Syllable("ке", millisOffset = 99, occurrenceCount = 12),
            Syllable("ки", millisOffset = 99, occurrenceCount = 86),
            Syllable("ко", millisOffset = 99, occurrenceCount = 128),
            Syllable("ку", millisOffset = 99, occurrenceCount = 42),
            Syllable("ла", millisOffset = 99, occurrenceCount = 51),
            Syllable("ле", millisOffset = 99, occurrenceCount = 41),
            Syllable("ли", millisOffset = 99, occurrenceCount = 131),
            Syllable("ло", millisOffset = 99, occurrenceCount = 76),
            Syllable("лу", millisOffset = 99, occurrenceCount = 30),
            Syllable("лы", millisOffset = 99, occurrenceCount = 41),
            Syllable("ль", millisOffset = 99, occurrenceCount = 55),
            Syllable("лю", millisOffset = 99, occurrenceCount = 20),
            Syllable("ля", millisOffset = 99, occurrenceCount = 38),
            Syllable("ма", millisOffset = 99, occurrenceCount = 59),
            Syllable("ме", millisOffset = 99, occurrenceCount = 52),
            Syllable("ми", millisOffset = 99, occurrenceCount = 38),
            Syllable("мо", millisOffset = 99, occurrenceCount = 45),
            Syllable("му", millisOffset = 99, occurrenceCount = 54),
            Syllable("на", millisOffset = 99, occurrenceCount = 231),
            Syllable("не", millisOffset = 99, occurrenceCount = 195),
            Syllable("ни", millisOffset = 99, occurrenceCount = 98),
            Syllable("но", millisOffset = 99, occurrenceCount = 93),
            Syllable("ну", millisOffset = 99, occurrenceCount = 36),
            Syllable("ны", millisOffset = 99, occurrenceCount = 43),
            Syllable("нь", millisOffset = 99, occurrenceCount = 51),
            Syllable("ня", millisOffset = 99, occurrenceCount = 27),
            Syllable("па", millisOffset = 99, occurrenceCount = 13),
            Syllable("пе", millisOffset = 99, occurrenceCount = 15),
            Syllable("пи", millisOffset = 99, occurrenceCount = 25),
            Syllable("по", millisOffset = 99, occurrenceCount = 166),
            Syllable("пу", millisOffset = 99, occurrenceCount = 17),
            Syllable("ра", millisOffset = 99, occurrenceCount = 116),
            Syllable("ре", millisOffset = 99, occurrenceCount = 82),
            Syllable("ри", millisOffset = 99, occurrenceCount = 88),
            Syllable("ро", millisOffset = 99, occurrenceCount = 100),
            Syllable("ру", millisOffset = 99, occurrenceCount = 58),
            Syllable("ры", millisOffset = 99, occurrenceCount = 12),
            Syllable("са", millisOffset = 99, occurrenceCount = 25),
            Syllable("се", millisOffset = 99, occurrenceCount = 58),
            Syllable("си", millisOffset = 99, occurrenceCount = 38),
            Syllable("со", millisOffset = 99, occurrenceCount = 89),
            Syllable("сь", millisOffset = 99, occurrenceCount = 53),
            Syllable("ся", millisOffset = 99, occurrenceCount = 82),
            Syllable("та", millisOffset = 99, occurrenceCount = 73),
            Syllable("те", millisOffset = 99, occurrenceCount = 79),
            Syllable("ти", millisOffset = 99, occurrenceCount = 43),
            Syllable("то", millisOffset = 99, occurrenceCount = 226),
            Syllable("ту", millisOffset = 99, occurrenceCount = 20),
            Syllable("ты", millisOffset = 99, occurrenceCount = 50),
            Syllable("ть", millisOffset = 99, occurrenceCount = 96),
            Syllable("хо", millisOffset = 99, occurrenceCount = 47),
            Syllable("ху", millisOffset = 99, occurrenceCount = 13),
            Syllable("ца", millisOffset = 99, occurrenceCount = 12),
            Syllable("це", millisOffset = 99, occurrenceCount = 19),
            Syllable("ча", millisOffset = 99, occurrenceCount = 28),
            Syllable("че", millisOffset = 99, occurrenceCount = 51),
            Syllable("чи", millisOffset = 99, occurrenceCount = 40),
            Syllable("ша", millisOffset = 99, occurrenceCount = 17),
            Syllable("ше", millisOffset = 99, occurrenceCount = 27),
            Syllable("ши", millisOffset = 99, occurrenceCount = 31),
            Syllable("шь", millisOffset = 99, occurrenceCount = 17),
            Syllable("щё", millisOffset = 99, occurrenceCount = 12),
            Syllable("а", millisOffset = 99),
            Syllable("б", millisOffset = 99),
            Syllable("в", millisOffset = 99),
            Syllable("г", millisOffset = 99),
            Syllable("д", millisOffset = 99),
            Syllable("е", millisOffset = 99),
            Syllable("ж", millisOffset = 99),
            Syllable("з", millisOffset = 99),
            Syllable("и", millisOffset = 99),
            Syllable("й", millisOffset = 99),
            Syllable("к", millisOffset = 99),
            Syllable("л", millisOffset = 99),
            Syllable("м", millisOffset = 99),
            Syllable("н", millisOffset = 99),
            Syllable("о", millisOffset = 99),
            Syllable("п", millisOffset = 99),
            Syllable("р", millisOffset = 99),
            Syllable("с", millisOffset = 99),
            Syllable("т", millisOffset = 99),
            Syllable("у", millisOffset = 99),
            Syllable("х", millisOffset = 99),
            Syllable("ц", millisOffset = 99),
            Syllable("ч", millisOffset = 99),
            Syllable("ш", millisOffset = 99),
            Syllable("щ", millisOffset = 99),
            Syllable("э", millisOffset = 99),
            Syllable("ю", millisOffset = 99),
            Syllable("я", millisOffset = 99),
            Syllable("ё", millisOffset = 99),
        )
    }
}