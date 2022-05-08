package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	// 1. 엔티티 직접 노출은 절대 금물!
	// 2. 양방향 매핑 되어있는 속성들 때문에 무한루프가 생긴다. @JsonIgnore를 이용해 한쪽을 끊어줘야한다
	// 3. Lazy 로딩 때문에 조회 시 실제 엔티티가 아닌 Proxy가 넘어와서 변환 에러가 발생한다.
	// Hybernate5Module을 이용해 Lazy로딩인 경우 조회시 무시하도록 설정해줘야한다.
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		for (Order order : orders) {
			order.getMember()
				 .getId(); // Lazy로딩 상태라면 이런식으로 강제 초기화 가능
			order.getDelivery()
				 .getId(); // Lazy로딩 상태라면 이런식으로 강제 초기화 가능
		}
		return orders;
	}

	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		return orders.stream()
					 .map(SimpleOrderDto::new)
					 .collect(Collectors.toList());
	}

	// fetch join 을 이용해 1 + N 문제 해결! 대부분의 성능 문제는 V3로 해결된다. 그래도 안되는 경우에는 V4를 적용해보자
	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		return orders.stream()
					 .map(SimpleOrderDto::new)
					 .collect(Collectors.toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderSimpleQueryRepository.findOrderDtos();
	}

	@Data
	static class SimpleOrderDto {

		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		// DTO에서 엔티티를 인자로 받는건 문제가 되지 않는다~~
		public SimpleOrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember()
							 .getName();
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery()
								.getAddress();
		}
	}
}
