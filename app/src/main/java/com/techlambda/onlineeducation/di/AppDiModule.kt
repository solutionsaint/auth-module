package com.techlambda.onlineeducation.di

import com.techlambda.onlineeducation.utils.AppApiRoutes.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDiModule {

    @Singleton
    @Provides
    fun provideKtorHttpClient(): HttpClient {
        return HttpClient(OkHttp){
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest)
            defaultRequest {
                url(BASE_URL)
            }
            engine {
                config {
                    retryOnConnectionFailure(true)
                    connectTimeout(5, TimeUnit.SECONDS)
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideAuthApi(client: HttpClient): AuthApi {
        return AuthApiImpl(client)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepository(authApi)
    }
}

}