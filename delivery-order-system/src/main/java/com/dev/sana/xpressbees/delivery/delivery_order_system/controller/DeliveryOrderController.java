package com.dev.sana.xpressbees.delivery.delivery_order_system.controller;

import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryOrder;
import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dev.sana.xpressbees.delivery.delivery_order_system.service.DeliveryOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DeliveryOrderController {
    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @PostMapping("/orders")
    public DeliveryOrder addOrder(@RequestBody DeliveryOrder order) {
        return deliveryOrderService.addOrder(order);
    }

    @GetMapping("/orders")
    public List<DeliveryOrder> fetchOrdersByStatus(@RequestParam DeliveryStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return deliveryOrderService.fetchOrdersByStatus(status, page, size);
    }

    @GetMapping("/customers/top")
    public List<Object[]> getTop3Customers() {
        return deliveryOrderService.getTop3Customers();
    }

    @GetMapping("/orders/status-count")
    public List<Object[]> getOrderStatusCounts() {
        return deliveryOrderService.getOrderStatusCounts();
    }

}
