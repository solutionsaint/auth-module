package com.techlambda.onlineeducation.ui.screen

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.navigation.BottomNavigationBar
import com.techlambda.onlineeducation.navigation.ToolBar
import com.techlambda.onlineeducation.network.ApiResult
import com.techlambda.onlineeducation.ui.screen.common.HandleApiResult
import com.techlambda.onlineeducation.ui.screen.common.NoDataView
import com.techlambda.onlineeducation.ui.screen.common.ShowLoadingScreen
import com.techlambda.onlineeducation.viewmodel.MainViewModel

@Composable
fun HomeScreen(navController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()
    Scaffold(
        topBar = { ToolBar(title = LocalContext.current.getString(R.string.home)) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->

        val apiResult by mainViewModel.homeScreenData.collectAsState()

        LaunchedEffect(Unit) {
            mainViewModel.getHomeScreenData()
        }

        HandleApiResult(padding,apiResult) { data ->
            data?.result?.discription?.let { ShowHtmlData(htmlString = it) } ?:
            run { NoDataView(message = LocalContext.current.getString(R.string.no_data_found)) }
        }

    }
}

@Composable
fun ShowHtmlData(htmlString: String){
        AndroidView(factory = { context ->
            /*TextView(context).apply {
            text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
        }*/

            try {
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Handle page load finished
                    }

                    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                        super.onReceivedError(view, errorCode, description, failingUrl)
                        Log.v( "WebViewClient","Error: $errorCode, $description, $failingUrl")
                    }
                }

                settings.javaScriptEnabled = true
                loadData(htmlString, "text/html", "UTF-8")
            }
            }catch (e:Exception){
                e.printStackTrace()
                TextView(context).apply {
                    text = "Error in loading data\n${e.message}"
                }
            }
        }, modifier = Modifier.padding(16.dp))
}
