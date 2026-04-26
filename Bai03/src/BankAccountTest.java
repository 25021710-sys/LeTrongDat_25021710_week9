import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bank & Account Unit Tests")
class BankAccountTest {

    private CheckingAccount account;
    private Bank bank;

    @BeforeEach
    void setUp() {
        account = new CheckingAccount(123456789L, 1000.0);
        bank = new Bank();
    }

    // =========================================================
    // Account Tests
    // =========================================================
    @Nested
    @DisplayName("Account")
    class AccountTests {

        @Test
        @DisplayName("Deposit should increase balance")
        void depositShouldIncreaseBalance() throws InvalidFundingAmountException {
            account.deposit(500.0);
            assertEquals(1500.0, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("Withdraw should decrease balance")
        void withdrawShouldDecreaseBalance() throws InsufficientFundsException, InvalidFundingAmountException {
            account.withdraw(300.0);
            assertEquals(700.0, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("Withdraw more than balance should throw")
        void withdrawMoreThanBalanceShouldThrow() {
            assertThrows(InsufficientFundsException.class, () -> account.withdraw(2000.0));
        }
    }

    // =========================================================
    // Bank Tests
    // =========================================================
    @Nested
    @DisplayName("Bank")
    class BankTests {

        @Test
        @DisplayName("Should read customers and accounts correctly")
        void shouldReadCustomersAndAccountsCorrectly() {
            String data = "Nguyen Van A: 123456789\n" +
                    "111 CHECKING 500.0\n" +
                    "222 SAVINGS 1000.0\n" +
                    "Tran Thi B: 987654321\n" +
                    "333 CHECKING 200.0\n";

            ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            bank.readCustomerList(input);

            assertEquals(2, bank.getCustomerList().size());

            Customer a = bank.getCustomerList().get(0);
            assertEquals("Nguyen Van A", a.getFullName());
            assertEquals(2, a.getAccountList().size());

            Customer b = bank.getCustomerList().get(1);
            assertEquals("Tran Thi B", b.getFullName());
            assertEquals(1, b.getAccountList().size());
        }

        @Test
        @DisplayName("Empty input should result in empty customer list")
        void emptyInputShouldResultInEmptyCustomerList() {
            ByteArrayInputStream input = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
            bank.readCustomerList(input);
            assertTrue(bank.getCustomerList().isEmpty());
        }
    }
}

