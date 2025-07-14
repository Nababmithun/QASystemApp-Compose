package com.example.qasystemappcompose.model


data class APIResponse(
    val record: List<QuestionRecord>,
    val metadata: Metadata
)

data class QuestionRecord(
    val id: String,
    val skip: Skip?,
    val type: String,
    val options: List<Option>?,  // Nullable: কিছুতে null আছে
    val referTo: ReferTo? = null,
    val question: Question,
    val validations: Validations? = null
)

data class Skip(
    val id: String
)

data class ReferTo(
    val id: String
)

data class Option(
    val value: String,
    val referTo: ReferTo? = null
)

data class Question(
    val slug: String
)

data class Validations(
    val regex: String
)

data class Metadata(
    val id: String,
    val `private`: Boolean,
    val createdAt: String,
    val name: String
)