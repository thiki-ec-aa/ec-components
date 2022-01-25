package com.thiki.ec.component.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"/sample"})
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping(value = "/order/{orderId}")
    public Order getOrder(@PathVariable("orderId") long orderId){
        return orderService.getOrder(orderId);

    }
}
