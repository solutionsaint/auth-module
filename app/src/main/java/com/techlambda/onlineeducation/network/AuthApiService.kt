import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignupRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignupResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class AuthApiService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun login(loginRequestModel: LoginRequestModel): LoginResponseModel {
        return httpClient.post("/auth/login") {
            setBody(loginRequestModel)
        }.body()
    }


    suspend fun signUp(signUpRequestModel: SignupRequestModel): SignupResponseModel {
        return httpClient.post("/auth/signup") {
            setBody(signUpRequestModel)
        }.body()
    }
}

