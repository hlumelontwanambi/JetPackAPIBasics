package vcmsa.projects.jetpackapibasics.ui

import android.R.attr.contentDescription
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import vcmsa.projects.jetpackapibasics.LoanItem
import vcmsa.projects.jetpackapibasics.LoanResponse
import vcmsa.projects.jetpackapibasics.ui.api.LoanViewModel

@Composable
fun LoanListScreen(
    loanViewModel: LoanViewModel,
    onNavigateToCreate: () -> Unit
) {
    val uiState by loanViewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(onClick = onNavigateToCreate) {
                    Icon(Icons.Default.Add, contentDescription = "Create Loan")
                }
                //New Button to refresh list
                FloatingActionButton(onClick = { loanViewModel.fetchLoans()}) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh Loans")
                }
            }
        }
    ) {
        paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.error != null -> Text(text= uiState.error!!, color = MaterialTheme.colorScheme.error)
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.loans) {
                        loan ->
                        LoanItem(
                            loan = loan,
                            onDelete = { loanViewModel.deleteLoan(loan.loanID)}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoanItem(loan: LoanResponse, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Loan ID: ${loan.loanID}", style = MaterialTheme.typography.titleMedium)
                Text("Loan Member: ${loan.memberID}", style = MaterialTheme.typography.bodySmall)
                Text("Loan Amount: ${loan.amount}", style = MaterialTheme.typography.bodyLarge)
                Text("Loan Message: ${loan.message}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Default Loan",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CreateLoanScreen(
    loanViewModel: LoanViewModel,
    onLoanCreated: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var memberID by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Added by Android app") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create A Loan", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = amount,
            onValueChange = { amount = it},
            label = { Text("Amount")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = memberID,
            onValueChange = { memberID = it},
            label = { Text("Member ID")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val amountDouble = amount.toDoubleOrNull()
                if (amountDouble != null && memberID.isNotBlank()) {
                    loanViewModel.createLoan(amountDouble, memberID, message)
                    onLoanCreated() // navigate back
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Loan")
        }
    }

}