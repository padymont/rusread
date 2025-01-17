package com.padym.rusread.viewmodels

import androidx.lifecycle.ViewModel
import com.padym.rusread.data.SyllableListDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManualListViewModel @Inject constructor(
    private val dao: SyllableListDao
) : ViewModel() {}