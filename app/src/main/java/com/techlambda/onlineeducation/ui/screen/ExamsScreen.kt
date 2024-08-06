package com.techlambda.onlineeducation.ui.screen

import androidx.compose.foundation.layout.Column
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.techlambda.onlineeducation.R
import com.techlambda.onlineeducation.model.ScreenDataModel
import com.techlambda.onlineeducation.navigation.ToolBar
import com.techlambda.onlineeducation.network.ApiResult
import com.techlambda.onlineeducation.ui.screen.common.HandleApiResult
import com.techlambda.onlineeducation.ui.screen.common.NoDataView
import com.techlambda.onlineeducation.ui.screen.common.ShowLoadingScreen
import com.techlambda.onlineeducation.viewmodel.MainViewModel

@Composable
fun ExamsScreen(navController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()
    Scaffold(
        topBar = { ToolBar(title = LocalContext.current.getString(R.string.exams)) }
    ) { padding ->

        val apiResult by mainViewModel.contributorsScreenData.collectAsState()

        LaunchedEffect(Unit) {
            mainViewModel.getContributorScreenData()
        }

        HandleApiResult(padding,apiResult) { data ->
            data?.result?.discription?.let { ShowHtmlData(htmlString = it) } ?:
            run { NoDataView(message = LocalContext.current.getString(R.string.no_data_found)) }
        }

    }
}
