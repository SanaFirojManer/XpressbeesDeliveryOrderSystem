package com.dev.sana.xpressbees.delivery.delivery_order_system.serviceImpl;

import com.dev.sana.xpressbees.delivery.delivery_order_system.DeliveryOrderSystemApplication;
import com.dev.sana.xpressbees.delivery.delivery_order_system.exception.OrderNotFoundExceptio;
import com.dev.sana.xpressbees.delivery.delivery_order_system.exception.OrderProcessingException;
import com.dev.sana.xpressbees.delivery.delivery_order_system.repository.DeliveryOrderRepository;
import jakarta.transaction.Transactional;
import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryOrder;
import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dev.sana.xpressbees.delivery.delivery_order_system.service.DeliveryOrderService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DeliveryOrderServiceImpl implements DeliveryOrderService {
    @Autowired
    private DeliveryOrderRepository orderRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(10); // Create a thread pool with 10 threads

    @Override
    public DeliveryOrder addOrder(DeliveryOrder order) {
        order.setDeliveryStatus(DeliveryStatus.PENDING);
        order.setCreatedAt(java.time.LocalDateTime.now());
        try {
            return orderRepository.save(order);
        } catch (Exception e){
            throw new OrderProcessingException("Error while saving the order");
        }

    }

    @Override
    public List<DeliveryOrder> fetchOrdersByStatus(DeliveryStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DeliveryOrder> orders = orderRepository.findByDeliveryStatus(status, pageable);
        if (orders.isEmpty()) {
            throw new OrderNotFoundExceptio("No orders found with status");
        }
        return orders.getContent();
    }

    @Override
    public List<Object[]> getTop3Customers() {
        try {
            return orderRepository.findTop3CustomersByDeliveredOrders(PageRequest.of(0, 3));
        } catch (Exception e){
            throw new OrderProcessingException("Error fetching top 3 customers");
        }
    }

    @Override
    public List<Object[]> getOrderStatusCounts() {
        try {
            return orderRepository.findOrderStatusCounts();
        } catch (Exception e){
            throw new OrderProcessingException("Error fetching order status counts");
        }
    }

    @Override
    @Transactional
    public void processOrder(DeliveryOrder order) {
        // Lock the order using pessimistic write lock
        order = orderRepository.findPendingOrdersForUpdate(DeliveryStatus.PENDING).stream().findFirst().orElse(null);
        if (order == null) {
            throw new OrderNotFoundExceptio("Order with PENDING status not found for processing.");
        }
        if (order != null) {
            order.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);
            orderRepository.save(order);

            // Create a copy of the order to use in the lambda, ensuring it's effectively final
            final DeliveryOrder orderToProcess = order;

            executorService.submit(() -> {
                try {
                    // Simulate delivery time
                    Thread.sleep(5000); // Simulate delivery time

                    // Update status after delivery
                    orderToProcess.setDeliveryStatus(DeliveryStatus.DELIVERED);
                    orderRepository.save(orderToProcess);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new OrderProcessingException("Error while processing the order.");
                }
            });
        }
    }
}