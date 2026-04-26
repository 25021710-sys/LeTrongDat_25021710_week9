package bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tai khoan tiet kiem - Lop nay thuc thi cac quy dinh ve rut tien và nap tien.
 */
public class SavingsAccount extends Account { // Đã sửa: Dấu ngoặc nhọn mở đầu dòng mới (Google Style yêu cầu cùng dòng)
    private Logger logger= LoggerFactory.getLogger(SavingsAccount.class);
    public static final int TYPE_DEPOSIT_SAVINGS=3;
    public static final double MAX_WITHDRAW=1000.0;
    public static final double MIN_BALANCE=5000.0;
    public static final int TYPE_WITHDRAW_SAVINGS=4;


    public SavingsAccount(long n, double b) {
        super(n, b);
    }

    @Override
    public void deposit(double amount) {
        // Đã sửa: Log không có cấu trúc, sử dụng System.err lộn xộn
        logger.debug("Giao dich dang xu ly...");
        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();
            // Đã sửa: Magic Number '3' (Nên dùng bank.Transaction.TYPE_DEPOSIT_SAVINGS)
            Transaction t = new Transaction(Transaction.TYPE_DEPOSIT_SAVINGS, amount, initialBalance, finalBalance);
            addTransaction(t);
            logger.info("Nap tien vao tai khoan {} thanh cong {} " , getAccountNumber(), amount);
            // Đã sửa: Catch Exception chung chung
        } catch (InvalidFundingAmountException e) {
            logger.error("So tien nap khong hop le:{} " ,e.getMessage(),e);
        } catch(Exception e){
            logger.error("Loi khong xac dinh khi nap tien:{}",e.getMessage(),e);
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance= getBalance();
        try {
            // Đã sửa: Magic Number '1000.0' thay vì hằng số MAX_WITHDRAW
            if (amount > MAX_WITHDRAW) {
                throw new InvalidFundingAmountException(amount);
            }
            // Đã sửa: Magic Number '5000.0' thay vì hằng số MIN_BALANCE
            if (initialBalance - amount < MIN_BALANCE) {
                throw new InsufficientFundsException(amount);
            }
            
            doWithdrawing(amount);
            double finalBalance = getBalance();
            
            // Đã sửa: Magic Number '4' (Nên dùng bank.Transaction.TYPE_WITHDRAW_SAVINGS)
            Transaction t = new Transaction(Transaction.TYPE_WITHDRAW_SAVINGS, amount, initialBalance, finalBalance);
            addTransaction(t);
            
            // Đã sửa: Log viết theo phong cách tùy tiện
            logger.debug("Rut {} thanh cong.So du con {} " , amount, finalBalance);
        } catch (Exception e) {
            // Đã sửa: Thiếu dấu ngoặc nhọn cho khối catch đơn dòng (tùy chuẩn)
            // Đã sửa: Log lỗi nhưng không ghi rõ lỗi gì hoặc stack trace
            logger.error("Rut tien bi loi!:{}",e.getMessage(),e);
        }
    }
}