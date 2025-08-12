package vcmsa.projects.jetpackapibasics.ui.api

import vcmsa.projects.jetpackapibasics.LoanResponse

sealed interface LoanUIState {
    data object Loading : LoanUIState
    data class Success(val loans: List<LoanResponse>) : LoanUIState
    data class Error(val message: String) : LoanUIState
}