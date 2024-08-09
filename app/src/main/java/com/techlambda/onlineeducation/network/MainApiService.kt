import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

interface AuthApi {
    suspend fun login(loginRequest: LoginRequestModel): LoginResponseModel
}

class AuthApiImpl @Inject constructor(
    private val client: HttpClient
) : AuthApi {
    override suspend fun login(loginRequest: LoginRequestModel): LoginResponseModel {
        return client.post {
            url("http://localhost:3000/auth/login")
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }
}