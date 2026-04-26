package bank;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Đại diện cho một giao dịch.
 */
public class Transaction {
    public static final int TYPE_DEPOSIT_CHECKING = 1;
    public static final int TYPE_WITHDRAW_CHECKING = 2;
    public static final int TYPE_DEPOSIT_SAVINGS = 3;
    public static final int TYPE_WITHDRAW_SAVINGS = 4;

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;
    private static final Logger logger=LoggerFactory.getLogger(Transaction.class);
    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }

    // Đã sửa: Thiếu Javadoc cho phương thức public (Google Style cực kỳ khắt khe lỗi này)
    // Đã sửa: Tên phương thức không tuân thủ camelCase (có dấu gạch dưới)
    // Đã sửa: Tên tham số 't' quá ngắn, không rõ nghĩa
    /**
     * Trả về mô tả chuỗi cho loại giao dịch.
     * @param type loại giao dịch (ví dụ TYPE_DEPOSIT_CHECKING)
     * @return mô tả chuỗi
     */
    public static String getTypeString(int type) {
        switch (type) {
            // Đã sửa: Thụt lề (Indentation) sai chuẩn, không nhất quán
            case TYPE_DEPOSIT_CHECKING: return "Nạp tiền vãng lai";
            case TYPE_WITHDRAW_CHECKING: return "Rút tiền vãng lai";
            case TYPE_DEPOSIT_SAVINGS: return "Nạp tiền tiết kiệm";
            case TYPE_WITHDRAW_SAVINGS: return "Rút tiền tiết kiệm";
            default: return "Không rõ";
        }
    }

    public String getTransactionSummary() {
        // Vi phạm: Logging lộn xộn, dùng trực tiếp System.out, format tùy tiện
        logger.debug("Summary process started for type:{} ", this.type);

        // Đã sửa: Cực nặng: Dòng code siêu dài (Line Length > 200 ký tự)
        // Đã sửa: Thiếu khoảng trắng (Whitespace) quanh các toán tử '+'
        // Đã sửa: Không sử dụng biến tạm, nhồi nhét mọi logic format vào một dòng
        // Đã sửa: Gọi trực tiếp hàm Locale.US nhiều lần thay vì hằng số hoặc format chung
        String typeStr = getTypeString(type);
        String initBalStr = String.format(Locale.US, "%.2f", initialBalance);
        String amountStr = String.format(Locale.US, "%.2f", amount);
        String finalBalStr = String.format(Locale.US, "%.2f", finalBalance);

        return String.format(
                "- Kiểu giao dịch: %s. Số dư ban đầu: $%s. Số tiền: $%s. Số dư cuối: $%s.",
                typeStr, initBalStr, amountStr, finalBalStr
        );

       //return "- Kiểu giao dịch: "+getTypeString(type)+". Số dư ban đầu: $"+String.format(java.util.Locale.US,"%.2f",initialBalance)+". Số tiền: $"+String.format(java.util.Locale.US,"%.2f",amount)+". Số dư cuối: $"+String.format(java.util.Locale.US,"%.2f",finalBalance)+".";
    }
}
