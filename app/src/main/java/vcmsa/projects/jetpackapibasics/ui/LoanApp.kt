package vcmsa.projects.jetpackapibasics.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import vcmsa.projects.jetpackapibasics.ui.api.LoanViewModel

object LoanScreenRoute {
    const val LIST = "list"
    const val CREATE = "create"
}

@Composable
fun LoanApp() {
    val loanViewModel: LoanViewModel = viewModel()
    var currentScreen by remember { mutableStateOf(LoanScreenRoute.LIST) }

    when (currentScreen) {
        LoanScreenRoute.LIST -> {
            LoanListScreen(
                loanViewModel = loanViewModel,
                onNavigateToCreate = {
                    currentScreen = LoanScreenRoute.CREATE
                }
            )
        }

        LoanScreenRoute.CREATE -> {
            CreateLoanScreen(
                loanViewModel = loanViewModel,
                onLoanCreated = {
                    currentScreen = LoanScreenRoute.LIST // Navigate back to the list
                }
            )
        }
    }
}