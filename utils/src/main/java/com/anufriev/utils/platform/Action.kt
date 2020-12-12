package com.anufriev.utils.platform

import androidx.annotation.StringRes

data class Action(@StringRes val text: Int, val func: () -> Unit = {})