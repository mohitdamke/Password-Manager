package mohit.dev.passwordmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import mohit.dev.passwordmanager.screen.AddPasswordScreen
import mohit.dev.passwordmanager.screen.EditPasswordScreen
import mohit.dev.passwordmanager.screen.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(Routes.Splash.route) {
            _root_ide_package_.android.window.SplashScreen(navController)
        }

        composable(Routes.Home.route) {
            HomeScreen(navController)
        }

        composable(Routes.AddPassword.route) {
            AddPasswordScreen(navController)
        }

        composable(
            route = Routes.EditPassword.route,
            arguments = listOf(navArgument("passwordId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("passwordId") ?: 0
            EditPasswordScreen(navController, id)
        }
//        navController.navigate(Screen.EditPassword.passId(passwordId))

    }
}
