package com.padym.rusread.viewmodels

data class SyllablePreview(
    val text: String = "",
    val isStarred: Boolean = false,
    val onClick: () -> Unit = {}
)