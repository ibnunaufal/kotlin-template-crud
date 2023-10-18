package id.co.ibnunaufal.template.data.model

import com.fasterxml.jackson.annotation.JsonProperty

data class InfoModel(
    @JsonProperty("_id")
    val id: String,
    @JsonProperty("id")
    val id2: String,
    val versionCode: String,
    val isUrgent: Boolean,
    val statusMigrasi: Boolean,
    val msg: List<String>,
    val date: String,
)
