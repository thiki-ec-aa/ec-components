package com.thiki.ec.component.sample;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class OrderRepoImpl implements OrderRepo{

    @Override
    public Order get(long id) {
        if (id == 10001){
            return new Order(10001, 10002, "zhangsan", 10003, "pen", 3, new BigDecimal(300));
        }else{
            //not found.
            return null;
        }
    }
}
