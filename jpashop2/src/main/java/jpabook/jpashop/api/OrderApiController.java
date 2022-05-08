package jpabook.jpashop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRespository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;
	private final OrderQueryRespository orderQueryRepository;

	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		for (Order order : orders) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.stream().forEach(o -> o.getItem().getCategories()); // 프록시 강제 초기화~
		}

		return orders;
	}

	/* 쿼리가 매우 많이 나감! */
	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		return orders.stream() // #1 Order 쿼리
			.map(OrderDto::new) // #2~#5 쿼리
			.collect(toList());
	}

	/* 쿼리 한방으로 해결되지만, 일대다 join fetch 한 경우 페이징이 메모리에서 이뤄지기 때문에 페이징을 쓰면 안된다!*/
	/* 그리고 join fetch는 컬렉션이 두개 이상일 때는 쓰면 데이터가 이상하게 날아올 수 있다!(여기서는 OrderItems 하나 뿐이라 괜춘)*/
	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithItem();
		return orders.stream()
			.map(OrderDto::new)
			.collect(toList());
	}

	/* 테이블을 정규화된 형식으로(중복값 없이) 당겨온다. v3에 비해 쿼리는 더 많지만 중복이 없기 때문에 페이징이 가능하다*/
	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3_page(
		@RequestParam(value = "offset", defaultValue = "0") int offset,
		@RequestParam(value = "limit", defaultValue = "100") int limit) {

		// `default_batch_fetch_size` 옵션을 통해 List<Order> 와 관련된 데이터를 `in` 쿼리를 통해 미리 가져온다
		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
		return orders.stream()
			.map(OrderDto::new)
			.collect(toList());
	}

	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> ordersV4() {
		return orderQueryRepository.findOrderQueryDtos();
	}

	@GetMapping("/api/v5/orders")
	public List<OrderQueryDto> ordersV5() {
		return orderQueryRepository.findAllByDto_optimization();
	}

	// 그냥 V5 쓰자
	@GetMapping("/api/v6/orders")
	public List<OrderQueryDto> ordersV6() {
		List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
		return flats.stream()
			.collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
					o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
				mapping(o -> new OrderItemQueryDto(o.getOrderId(),
					o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
			)).entrySet().stream()
			.map(e -> new OrderQueryDto(e.getKey().getOrderId(),
				e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
				e.getKey().getAddress(), e.getValue()))
			.collect(toList());
	}

	@Getter
	static class OrderDto {

		private Long orderId;
		private String name;
		private LocalDateTime orderDate; //주문시간
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems;

		// List<OrderItem>로 내보내는 것 역시 엔티티를 그대로 노출하여 엔티티 수정 시 API도 변하게되는 결과를 초래하므로
		// List<OrderItemDto>로 변환하여 내보낸다
		public OrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember().getName(); // #2 Member 쿼리
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery().getAddress(); // #3 Delivery 쿼리
			order.getOrderItems().stream()
				.forEach(o -> o.getItem().getName()); // #4, #5 OrderItems 쿼리, Item 쿼리 까지!!
			this.orderItems = order.getOrderItems().stream()
				.map(OrderItemDto::new)
				.collect(toList());
		}
	}

	@Getter
	static class OrderItemDto {

		private String itemName;
		private int orderPrice;
		private int count;

		public OrderItemDto(OrderItem orderItem) {
			this.itemName = orderItem.getItem().getName();
			this.orderPrice = orderItem.getOrderPrice();
			this.count = orderItem.getCount();
		}
	}
}
