package mohit.dev.passwordmanager.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Lock : Routes("lock")
    object Home : Routes("home")
    object AddPassword : Routes("add_password")
    object EditPassword : Routes("edit_password/{passwordId}") {
        fun passId(id: Int) = "edit_password/$id"
    }
}
