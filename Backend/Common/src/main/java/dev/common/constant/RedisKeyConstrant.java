package dev.common.constant;

import java.util.UUID;

public class RedisKeyConstrant {
    public static String invalidTokenKey(UUID tokenId){
        return String.format("InvalidToken:%s", tokenId);
    }

    public static String PRODUCT_KEY(int id){
        return String.format("Product:id:%s", id);
    }

    public static String USER_NAME_KEY(String userName){
        return String.format("UserName:%s", userName);
    }

    public static String COMMENTS_OF_PRODUCT_KEY(int productId){
        return String.format("Product:comments:%s", productId);
    }

    public static String medicinesInvoiceDetail(UUID invoiceId){
        return String.format("Medicines:Invoice:%s", invoiceId);
    }
}
