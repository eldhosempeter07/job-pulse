package com.example.jobpulse

import java.util.Date

data class Post(
    val title: String? = null,
    val desc: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
    val createdTime: Date? = null,
)