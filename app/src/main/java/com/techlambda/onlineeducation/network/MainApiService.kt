import com.techlambda.onlineeducation.model.Request.LoginRequestModel
import com.techlambda.onlineeducation.model.Request.SignupRequestModel
import com.techlambda.onlineeducation.model.Response.LoginResponseModel
import com.techlambda.onlineeducation.model.Response.SignupResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

interface MainApiService {

    @POST("auth/login")
    suspend fun login(@Body loginRequestModel: LoginRequestModel): Response<LoginResponseModel>

    @POST("/auth/signup")
    suspend fun signUp(@Body signUpRequestModel: SignupRequestModel): Response<SignupResponseModel>
}

