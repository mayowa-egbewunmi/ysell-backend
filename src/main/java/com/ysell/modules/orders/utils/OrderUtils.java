package com.ysell.modules.orders.utils;

import com.ysell.jpa.entities.enums.SaleType;

import java.math.BigDecimal;

/**
 * @author tchineke
 * @since 13 March, 2021
 */
public class OrderUtils {

    public static int getIntValueBySaleType(SaleType saleType) {
        return saleType == SaleType.SALE ? 1 : saleType == SaleType.RETURN ? -1 : 0;
    }


    public static BigDecimal getDecimalValueBySaleType(SaleType saleType) {
        return BigDecimal.valueOf(getIntValueBySaleType(saleType));
    }
}