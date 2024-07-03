package io.github.drumber.kitsune.data.repository

import io.github.drumber.kitsune.data.mapper.AuthMapper.toLocalAccessToken
import io.github.drumber.kitsune.data.source.local.auth.AccessTokenLocalDataSource
import io.github.drumber.kitsune.data.source.local.auth.LocalAccessToken
import io.github.drumber.kitsune.data.source.network.auth.AccessTokenNetworkDataSource
import io.github.drumber.kitsune.data.source.network.auth.model.ObtainAccessToken
import io.github.drumber.kitsune.data.source.network.auth.model.RefreshAccessToken
import io.github.drumber.kitsune.util.logD
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AccessTokenRepository(
    private val localAccessTokenDataSource: AccessTokenLocalDataSource,
    private val remoteAccessTokenDataSource: AccessTokenNetworkDataSource
) {

    private val mutex = Mutex()

    fun getAccessToken(): LocalAccessToken? {
        return localAccessTokenDataSource.loadAccessToken()
    }

    fun hasAccessToken(): Boolean {
        return getAccessToken() != null
    }

    fun clearAccessToken() {
        localAccessTokenDataSource.clearAccessToken()
    }

    suspend fun obtainAccessToken(username: String, password: String): LocalAccessToken {
        mutex.withLock {
            val accessToken = remoteAccessTokenDataSource.obtainAccessToken(
                ObtainAccessToken(
                    username = username,
                    password = password
                )
            ).toLocalAccessToken()
            localAccessTokenDataSource.storeAccessToken(accessToken)
            return accessToken
        }
    }

    suspend fun refreshAccessToken(): LocalAccessToken {
        val refreshToken = getAccessToken()?.refreshToken
            ?: throw IllegalStateException("No refresh token available. Are you logged in?")

        mutex.withLock {
            // Check if the access token was changed by a concurrent request
            val localAccessToken = getAccessToken()
            if (localAccessToken != null && localAccessToken.refreshToken != refreshToken) {
                logD("Access token was updated by a concurrent request. Returning the updated token.")
                return localAccessToken
            }

            val accessToken = remoteAccessTokenDataSource.refreshToken(
                RefreshAccessToken(
                    refreshToken = refreshToken
                )
            ).toLocalAccessToken()
            localAccessTokenDataSource.storeAccessToken(accessToken)
            return accessToken
        }
    }
}