package com.tovelop.maphant.dto

data class ValidationSignupDTO(
    val email: String?,
    val nickName: String?,
    val phoneNum: String?,
    val password: String?,
    val passwordChk: String?
)
