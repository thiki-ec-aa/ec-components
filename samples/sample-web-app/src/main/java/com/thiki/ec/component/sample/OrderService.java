package com.thiki.ec.component.sample;

import lombok.val;
import net.thiki.ec.component.exception.AssertionException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderService {

    @Resource
    private OrderRepo orderRepo;

    public Order getOrder(long orderId) {
        val order = orderRepo.get(orderId);
        if (order == null){
            throw new AssertionException(10001, String.format("The order with id[%s] is not found.", orderId));
        }
        return order;
    }

}
