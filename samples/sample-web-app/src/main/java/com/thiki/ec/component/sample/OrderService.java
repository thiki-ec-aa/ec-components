package com.thiki.ec.component.sample;

import com.thiki.ec.component.sample.ex.Codes;
import kotlin.Pair;
import lombok.val;
import net.thiki.ec.component.exception.AssertionExceptionKt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderService {

    @Resource
    private OrderRepo orderRepo;

    public Order getOrder(long orderId) {
        val order = orderRepo.get(orderId);
        if (order == null){
            AssertionExceptionKt.badRequestError(
                    Codes.OrderNotFound.code,
                    "order with id{} not found.",
                    new Pair<>("orderId", String.valueOf(orderId))
            );
//            throw new AssertionException(10001, String.format("The order with id[%s] is not found.", orderId));
        }
        return order;
    }

}
