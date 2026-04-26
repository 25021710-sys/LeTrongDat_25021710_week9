package bank;// Đã sửa : Wildcard import
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bank {
    private static final Logger logger=LoggerFactory.getLogger(Bank.class);
    // Đã sửa: Tên biến không rõ nghĩa, viết tắt sai chuẩn camelCase
    private List<Customer> customerList;

    public Bank() {
        this.customerList = new ArrayList<Customer>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    // Vi phạm: Thụt đầu dòng (Indentation) lung tung và Javadoc thiếu tag @param
    /**
     * Set danh sach khach hang
     * @param customerList danh sách khách hàng cần gán
     */
    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customerList = new ArrayList<Customer>();
        } else {
            this.customerList = customerList;
        }
    }

    /**
     * Ham nay rat dai và khó doc
     * @param inputStream luồng dữ liệu đầu vào
     */
    public void readCustomerList(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Customer current = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                int last = line.lastIndexOf(':');
                if (last <= 0) continue;

                String token = line.substring(last + 1).trim();

                if (token.matches("\\d{9}")) { // TODO: đưa regex vào hằng số
                    String name = line.substring(0, last).trim();
                    current = new Customer(Long.parseLong(token), name);
                    customerList.add(current);
                    logger.info("Thêm khách hàng {}", name);
                } else if (current != null) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3) {
                        long num = Long.parseLong(parts[0]);
                        double bal = Double.parseDouble(parts[2]);

                        switch (parts[1]) {
                            case "CHECKING":
                                current.addAccount(new CheckingAccount(num, bal));
                                break;
                            case "SAVINGS":
                                current.addAccount(new SavingsAccount(num, bal));
                                break;
                            default:
                                logger.warn("Loại tài khoản không hợp lệ: {}", parts[1]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Lỗi khi đọc danh sách khách hàng", e);
        }
    }

    public String getCustomersInfoByIdOrder() {
        // Đã sửa: Dùng Anonymous class thay vì Lambda, thụt lề sai
        customerList.sort((o1, o2) -> Long.compare(o1.getIdNumber(), o2.getIdNumber())
        );

        // Đã sửa: Cộng chuỗi (String concatenation) trong vòng lặp - Cực tệ cho performance
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < customerList.size(); i++) {
            sb.append(customerList.get(i).getCustomerInfo());
            if (i < customerList.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String buildCustomerInfo(List<Customer> customers){
        return customers.stream().map(Customer::getCustomerInfo).collect(Collectors.joining("\n"));
    }

    public String getCustomersInfoByNameOrder() {
        // Đã sửa : Logic trùng lặp nhiều với hàm trên (Code Duplication)
        List<Customer> copy = new ArrayList<Customer>(customerList);
        copy.sort ((c1,c2)->{
            int res = c1.getFullName().compareTo(c2.getFullName());
            return res != 0 ? res : Long.compare(c1.getIdNumber(), c2.getIdNumber());
        });

        // Đã sửa: Dòng code quá dài, không ngắt dòng
        return buildCustomerInfo(copy);
    }
}