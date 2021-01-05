package com.anufriev.data.entity

data class Contact(
    val id: Long,
    val name: String,
    val phones: List<String>
)