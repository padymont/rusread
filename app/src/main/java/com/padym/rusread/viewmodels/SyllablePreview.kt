package com.padym.rusread.viewmodels

data class SyllablePreview(
    val text: String,
    val isStarred: Boolean,
    val onClick: () -> Unit = {}
)