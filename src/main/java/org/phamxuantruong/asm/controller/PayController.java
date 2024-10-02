package org.phamxuantruong.asm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.phamxuantruong.asm.DTO.PaymentRestDTO;
import org.phamxuantruong.asm.config.Config;
import org.phamxuantruong.asm.entity.*;
import org.phamxuantruong.asm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PayController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestBody OrderRequest orderRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        if (orderRequest == null || orderRequest.getAccount() == null || orderRequest.getOrderDetails() == null) {
            return ResponseEntity.badRequest().body("Thông tin đơn hàng không hợp lệ");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode orderData = mapper.valueToTree(orderRequest);

        // Gọi phương thức create từ OrderService
        Order order = orderService.create(orderData);

        long amount = orderRequest.getOrderDetails().stream()
                .mapToLong(detail -> (long) (detail.getPrice() * detail.getQuantity()))
                .sum();
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(request);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Nhân với 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // Tính toán giá trị SecureHash
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(URLEncoder.encode(vnp_SecureHash, StandardCharsets.US_ASCII.toString()));
        String paymentUrl = Config.vnp_PayUrl + "?" + query.toString();

        PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
        paymentRestDTO.setStatus("OK");
        paymentRestDTO.setMessage("Successfully");
        paymentRestDTO.setURL(paymentUrl);
        System.out.println("paymentUrl: " + paymentUrl);
        System.out.println("vnp_SecureHash: " + vnp_SecureHash);
        System.out.println("vnp_Params: " + vnp_Params);
        return ResponseEntity.ok(paymentRestDTO);
    }

    @GetMapping("/vnpay_return")
    public String vnPayReturn(HttpServletRequest request, HttpSession session, Model model) throws UnsupportedEncodingException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHash");

        String signValue = Config.hashAllFields(fields);

        String paymentStatus;
        String message;

        if (signValue.equals(vnp_SecureHash)) {
            String responseCode = fields.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                paymentStatus = "success";
                message = "Payment Successful!";


            } else {
                paymentStatus = "failure";
                message = "Payment Failed: " + responseCode;
            }
        } else {
            paymentStatus = "failure";
            message = "Invalid signature";
        }

        // Sử dụng UriComponentsBuilder để xây dựng URL
        String redirectUrl = UriComponentsBuilder.fromUriString("/order/checkout")
                .queryParam("paymentStatus", paymentStatus)
                .queryParam("message", message)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        // Chuyển hướng đến trang checkout
        return "redirect:" + redirectUrl;
    }
}
