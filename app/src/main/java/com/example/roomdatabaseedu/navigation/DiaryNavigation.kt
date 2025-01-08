package com.example.roomdatabaseedu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.roomdatabaseedu.db.DiaryDAO
import com.example.roomdatabaseedu.screens.detail.DetailScreen
import com.example.roomdatabaseedu.screens.home.HomeScreen

@Composable
fun DiaryNavigation(dao: DiaryDAO){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DiaryScreens.HOMESCREEN.name
    ) {
        composable(route = DiaryScreens.HOMESCREEN.name){
            HomeScreen(navController, dao)
        }

        composable(
            route = DiaryScreens.DETAILSCREEN.name+"/{diaryIdx}",
            arguments = listOf(navArgument("diaryIdx"){type = NavType.IntType})
        ){
            DetailScreen(navController, it.arguments?.getInt("diaryIdx"), dao)
        }
    }
}