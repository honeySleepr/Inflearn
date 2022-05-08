package jpabook.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// Respository는 엔티티 조회용
// Query는 화면이나 API와 의존관계가 있음
// 그래서 따로 repository 패키지와 분리한것
@Repository
@RequiredArgsConstructor
public class OrderQueryRespository {

	private final EntityManager em;

	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> result = findOrders(); // #1 쿼리 1번

		result.forEach(o -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // #2 쿼리 N번
			o.setOrderItems(orderItems);
		});
		return result;
	}

	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> result = findOrders();

		List<Long> orderIds = result.stream()
			.map(o -> o.getOrderId())
			.collect(Collectors.toList());

		List<OrderItemQueryDto> orderItems =
			em.createQuery(
					"select new " +
					"jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.id, i.name, oi.orderPrice, oi.count)" +
					" from OrderItem oi" +
					" join oi.item i" +
					" where oi.order.id in :orderIds", OrderItemQueryDto.class) // `in` 사용
				.setParameter("orderIds", orderIds)
				.getResultList();

		// 메모리에 Map으로 값을 저장하여 최적화에 사용한다
		Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
			.collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

		result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

		return result;
	}

	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery(
				"select new " +
				"jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.id, i.name, oi.orderPrice, oi.count)" +
				" from OrderItem oi" +
				" join oi.item i" +
				" where oi.order.id = :orderId", OrderItemQueryDto.class)
			.setParameter("orderId", orderId)
			.getResultList();
	}

	private List<OrderQueryDto> findOrders() {
		return em.createQuery(
				"select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, m.address)" +
				" from Order o" +
				" join o.member m" +
				" join o.delivery d", OrderQueryDto.class)
			.getResultList();
	}

	public List<OrderFlatDto> findAllByDto_flat() {
		return em.createQuery(
				"select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) " +
				" from Order o" +
				" join o.member m" +
				" join o.delivery d" +
				" join o.orderItems oi" +
				" join oi.item i", OrderFlatDto.class)
			.getResultList();
	}
}
