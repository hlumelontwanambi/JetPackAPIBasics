package vcmsa.projects.jetpackapibasics.ui.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vcmsa.projects.jetpackapibasics.LoanRequest
import vcmsa.projects.jetpackapibasics.LoanResponse
import vcmsa.projects.jetpackapibasics.RetrofitClient

data class LoanScreenState(
    val loans: List<LoanResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoanViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoanScreenState())
    val uiState: StateFlow<LoanScreenState> = _uiState

    //The `init` block immediately calls `fetchLoans()` to load data
    // when the viewModel is instantiated
    init {
        fetchLoans()
    }

    fun fetchLoans() {
        _uiState.value = uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val loans = RetrofitClient.instance.getLoans()
                _uiState.value = _uiState.value.copy(isLoading = false, loans = loans)
            } catch (e: Exception) {
                _uiState.value  = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun createLoan(amount: Double, memberID: String, message: String) {
        viewModelScope.launch {
            try {
                val newLoan = LoanRequest(amount, memberID, message)
                RetrofitClient.instance.createLoan(newLoan)
                //After successfully creating, refresh the list to show the new item
                fetchLoans()
            } catch (e: Exception){
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to create loan: ${e.message}")
            }
        }
    }

    fun deleteLoan(loanID: Int) {
        viewModelScope.launch {
            try {
                // Change is here
                val response = RetrofitClient.instance.deleteLoan(loanID)
                if (response.isSuccessful) {
                    // The deletion was successful (e.g., status 204), so refresh the list
                    fetchLoans()
                } else {
                    // The server returned an error code (e.g., 404 Not Found, 500 Server Error)
                    _uiState.value = _uiState.value.copy(error = "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                //This catches network errors, like no internet connection
                _uiState.value = _uiState.value.copy(error = "Failed to delte loan: ${e.message}")
            }
        }
    }
}