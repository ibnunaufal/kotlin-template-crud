package id.co.ibnunaufal.template.data.response

import androidx.annotation.Keep

@Keep
data class LoginResponse(
    val firstLogin: Boolean,
    val user: User
)

class User(
    val address: String,
    val dateOfBirth: String,
    val email: String,
    val gender: String,
    val id: String,
    val lastCompanyId: String,
    val lastService: String,
    val maritalStatus: String,
    val name: String,
    val nik: String,
    val phone: Any,
    val photoUrl: String,
    val placeOfBirth: String,
    val regDate: String,
    val religion: String,
    val username: String,
    val validationStatus: Boolean
)