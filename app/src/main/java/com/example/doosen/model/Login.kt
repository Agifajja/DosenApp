package com.example.doosen.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("refresh_expires_in") val refreshExpiresIn: Int,
    @SerializedName("not-before-policy") val notBeforePolicy: Int,
    @SerializedName("session_state") val sessionState: String,
    @SerializedName("scope") val scope: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("preferred_username") val preferredUsername: String? = null,
    @SerializedName("given_name") val givenName: String? = null,
    @SerializedName("family_name") val familyName: String? = null,
    @SerializedName("email") val email: String? = null
)
