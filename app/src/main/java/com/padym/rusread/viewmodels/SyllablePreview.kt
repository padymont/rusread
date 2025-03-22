package com.padym.rusread.viewmodels

data class SyllablePreview(
    val text: String = "",
    val isSelected: Boolean = false,
    val isEnabled: Boolean = true,
    val isStarred: Boolean = false,
    val onClick: () -> Unit = {}
)