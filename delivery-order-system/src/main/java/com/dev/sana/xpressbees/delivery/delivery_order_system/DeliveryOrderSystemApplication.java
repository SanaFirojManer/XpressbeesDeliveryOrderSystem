package com.dev.sana.xpressbees.delivery.delivery_order_system;

import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryOrder;
import com.dev.sana.xpressbees.delivery.delivery_order_system.model.DeliveryStatus;
import jakarta.persistence.LockModeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DeliveryOrderSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryOrderSystemApplication.class, args);
	}

	@Repository
	public static interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Integer> {

		// Pessimistic locking to update pending orders
		@Lock(LockModeType.PESSIMISTIC_WRITE)
		@Query("SELECT o FROM DeliveryOrder o WHERE o.deliveryStatus = :status ORDER BY o.createdAt ASC")
		List<DeliveryOrder> findPendingOrdersForUpdate(DeliveryStatus status);

		// Query to find top 3 customers by the number of delivered orders
		@Query("SELECT customerName, COUNT(orderId) AS orderCount FROM DeliveryOrder " +
				"WHERE deliveryStatus = 'DELIVERED' " +
				"GROUP BY customerName " +
				"ORDER BY orderCount DESC")
		List<Object[]> findTop3CustomersByDeliveredOrders(Pageable pageable);

		// Query to get the count of orders grouped by delivery status
		@Query("SELECT deliveryStatus, COUNT(orderId) FROM DeliveryOrder GROUP BY deliveryStatus")
		List<Object[]> findOrderStatusCounts();

		// Find a DeliveryOrder by its ID
		Optional<DeliveryOrder> findById(Integer orderId);

		// Paginated query to find DeliveryOrders by delivery status
		Page<DeliveryOrder> findByDeliveryStatus(DeliveryStatus status, Pageable pageable);
	}
}
