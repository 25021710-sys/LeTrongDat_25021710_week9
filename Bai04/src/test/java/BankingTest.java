import bank.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankingTest {

    private CheckingAccount checkingAccount;
    private SavingsAccount savingsAccount;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        // Khởi tạo dữ liệu mẫu trước mỗi hàm test
        customer = new Customer(123456789L, "Nguyen Van A");

        // Khởi tạo CheckingAccount với 10,000$
        checkingAccount = new CheckingAccount(1001L, 10000.0);

        // Khởi tạo SavingsAccount với 10,000$
        savingsAccount = new SavingsAccount(1002L, 10000.0);

        customer.addAccount(checkingAccount);
        customer.addAccount(savingsAccount);
    }

    // ==========================================
    // TEST CHO TÀI KHOẢN VÃNG LAI (CHECKING)
    // ==========================================

    @Test
    public void testCheckingAccountDeposit_ValidAmount() {
        checkingAccount.deposit(2000.0);
        assertEquals(12000.0, checkingAccount.getBalance(), "Số dư phải tăng lên 12,000$");
        assertEquals(1, checkingAccount.getTransactionList().size(), "Phải có 1 giao dịch được lưu");
    }

    @Test
    public void testCheckingAccountWithdraw_ValidAmount() {
        checkingAccount.withdraw(3000.0);
        assertEquals(7000.0, checkingAccount.getBalance(), "Số dư phải giảm xuống 7,000$");
    }

    @Test
    public void testCheckingAccountWithdraw_InsufficientFunds() {
        // Rút quá số dư hiện có (10,000)
        checkingAccount.withdraw(15000.0);

        // Vì class CheckingAccount đã catch Exception bên trong hàm withdraw,
        // số dư sẽ không thay đổi và giao dịch không được thêm vào
        assertEquals(10000.0, checkingAccount.getBalance(), "Số dư không được đổi vì giao dịch thất bại");
        assertEquals(0, checkingAccount.getTransactionList().size());
    }

    // ==========================================
    // TEST CHO TÀI KHOẢN TIẾT KIỆM (SAVINGS)
    // ==========================================

    @Test
    public void testSavingsAccountWithdraw_ValidAmount() {
        // Rút 800$ (Thỏa mãn < 1000$ và số dư còn lại > 5000$)
        savingsAccount.withdraw(800.0);
        assertEquals(9200.0, savingsAccount.getBalance());

        // Kiểm tra lịch sử giao dịch
        List<Transaction> history = savingsAccount.getTransactionList();
        assertEquals(1, history.size());
        assertEquals(Transaction.TYPE_WITHDRAW_SAVINGS, history.get(0).getType());
    }

    @Test
    public void testSavingsAccountWithdraw_ExceedMaxWithdraw() {
        // Cố tình rút 1500$ (Vượt quá hằng số MAX_WITHDRAW = 1000$)
        savingsAccount.withdraw(1500.0);

        // Giao dịch sẽ bị catch Exception InvalidFundingAmountException, số dư giữ nguyên
        assertEquals(10000.0, savingsAccount.getBalance(), "Số dư phải giữ nguyên do rút quá 1000$");
        assertEquals(0, savingsAccount.getTransactionList().size());
    }

    @Test
    public void testSavingsAccountWithdraw_BelowMinBalance() {
        // Khởi tạo một tài khoản tiết kiệm có đúng 5500$
        SavingsAccount poorSavings = new SavingsAccount(1003L, 5500.0);

        // Cố tình rút 600$ (Dưới hạn mức 1000$, nhưng làm số dư còn 4900$ < MIN_BALANCE = 5000$)
        poorSavings.withdraw(600.0);

        // Giao dịch sẽ bị catch Exception InsufficientFundsException
        assertEquals(5500.0, poorSavings.getBalance(), "Số dư giữ nguyên vì vi phạm số dư tối thiểu");
    }

    // ==========================================
    // TEST CHO KHÁCH HÀNG (CUSTOMER)
    // ==========================================

    @Test
    public void testCustomerInfo() {
        String expectedInfo = "Số CMND: 123456789. Họ tên: Nguyen Van A.";
        assertEquals(expectedInfo, customer.getCustomerInfo());
    }

    @Test
    public void testAddAndRemoveAccount() {
        assertEquals(2, customer.getAccountList().size());

        customer.removeAccount(checkingAccount);
        assertEquals(1, customer.getAccountList().size());
        assertFalse(customer.getAccountList().contains(checkingAccount));
    }
    @Test
    public void testFilePathCompatibility() {
        // SỬA TẠI ĐÂY: Sử dụng File.separator để tự động thích ứng với hệ điều hành
        String expectedPath = "folder" + File.separator + "file.txt";

        File myFile = new File("folder", "file.txt");

        // Giờ đây test này sẽ PASS trên cả Windows, Ubuntu và MacOS
        assertEquals(expectedPath, myFile.getPath(), "Đường dẫn phải khớp với định dạng của hệ điều hành hiện tại");
    }
}
