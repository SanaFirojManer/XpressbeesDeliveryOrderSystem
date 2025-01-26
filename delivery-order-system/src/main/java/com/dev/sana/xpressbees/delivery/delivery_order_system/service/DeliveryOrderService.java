package com.dev.sana.xpressbees.delivery.delivery_order_system.service;

import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryOrder;
import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryStatus;

import java.util.List;

public interface DeliveryOrderService {
    DeliveryOrder addOrder(DeliveryOrder order);
    List<DeliveryOrder> fetchOrdersByStatus(DeliveryStatus status, int page, int size);
    List<Object[]> getTop3Customers();
    List<Object[]> getOrderStatusCounts();
    void processOrder(DeliveryOrder order);
}
