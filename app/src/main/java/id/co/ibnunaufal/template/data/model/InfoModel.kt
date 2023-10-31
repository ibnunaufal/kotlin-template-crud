package id.co.ibnunaufal.template.data.model

import com.fasterxml.jackson.annotation.JsonProperty

data class InfoModel(
    val _id: String,
    val id: String,
    val versionCode: String,
    val isUrgent: Boolean,
    val statusMigrasi: Boolean,
    val msg: List<String>,
    val date: String,
)

data class PostInfoModel(
    val id: String,
    val versionCode: String,
    val isUrgent: Boolean,
    val statusMigrasi: Boolean,
    val msg: List<String>,
    val date: String
)
