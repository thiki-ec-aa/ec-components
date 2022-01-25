package com.thiki.ec.component.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private long id;
    private long userId;
    private String userName;
    private long goodsId;
    private String goodsName;
    private int count;
    private BigDecimal price;

}
