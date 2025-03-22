package com.padym.rusread.viewmodels

data class PreviewGroup(
    val id: Int = 0,
    val isPreviousEnabled: Boolean = false,
    val isNextEnabled: Boolean = false,
    val syllables: List<SyllablePreview> = emptyList()
)