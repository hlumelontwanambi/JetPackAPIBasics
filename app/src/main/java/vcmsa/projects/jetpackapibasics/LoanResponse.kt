package vcmsa.projects.jetpackapibasics

data class LoanResponse(
    val loanID: Int,
    val amount: Double,
    val memberID: String,
    val message: String
)

data class LoanRequest(
    val amount: Double,
    val memberID: String,
    val message: String
)