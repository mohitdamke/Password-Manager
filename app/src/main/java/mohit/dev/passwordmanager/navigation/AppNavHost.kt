package mohit.dev.passwordmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mohit.dev.passwordmanager.screen.AddPasswordScreen
import mohit.dev.passwordmanager.screen.EditPasswordScreen
import mohit.dev.passwordmanager.screen.HomeScreen
import mohit.dev.passwordmanager.screen.PinLockScreen
import mohit.dev.passwordmanager.screen.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }

        composable(Routes.Home.route) {
            HomeScreen(
                onAddClick = { navController.navigate(Routes.AddPassword.route) },
                onEditClick = { id ->
                    navController.navigate(Routes.EditPassword.passId(id))
                }
            )
        }


        composable(Routes.AddPassword.route) {
            AddPasswordScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Lock.route) {
            PinLockScreen(onUnlock = {
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.Lock.route) { inclusive = true }
                }
            })
        }

        composable(
            route = Routes.EditPassword.route
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("passwordId")?.toInt()!!
            EditPasswordScreen(navController = navController, passwordId = id)
        }

    }
}
