package vcmsa.projects.jetpackapibasics

import android.R.attr.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vcmsa.projects.jetpackapibasics.ui.api.LoanUIState
import vcmsa.projects.jetpackapibasics.ui.api.LoanViewModel

@Composable
fun LoanItem(loan: LoanResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Loan ID: ${loan.loanID}", style = MaterialTheme.typography.titleMedium)
            Text("Loan Member: ${loan.memberID}", style = MaterialTheme.typography.bodySmall)
            Text("Loan Amount: ${loan.amount}", style = MaterialTheme.typography.bodyLarge)
            Text("Loan Message: ${loan.message}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun LoanList(loans: List<LoanResponse>, modifier: Modifier = Modifier)
{
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(loans) {
            loan -> LoanItem(loan = loan)
        }
    }
}

@Composable
fun LoanScreen(loanViewModel: LoanViewModel = viewModel())
{
    val uiState by loanViewModel.uiState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is LoanUIState.Loading -> {
                CircularProgressIndicator()
            }
            is LoanUIState.Success -> {
                LoanList(loans = state.loans)
            }
            is LoanUIState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { loanViewModel.fetchLoans() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}