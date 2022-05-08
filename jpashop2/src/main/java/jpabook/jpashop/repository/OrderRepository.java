package jpabook.jpashop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	public List<Order> findAllByString(OrderSearch orderSearch) {
//language=JPAQL
		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;
//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}
//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}
		TypedQuery<Order> query = em.createQuery(jpql, Order.class)
									.setMaxResults(1000); //최대 1000건
		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		return query.getResultList();
	}

	// SimpleV4에 비해 쿼리 성능은 쪼끔 떨어지지만 재사용성이 좋다
	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery("select o from Order o" +
							  " join fetch o.member m" +
							  " join fetch o.delivery d", Order.class)
				 .getResultList();
	}

	// OrderApi V3
	public List<Order> findAllWithMemberDelivery(int offset, int limit) {
		// ToOne 관계인 데이터는 전부 fetch join으로 당겨온다.(ToOne 관계 안에 또 ToOne 관계가 있으면 걔도 마찬가지)
		// ToMany 인 애들은 fetch join을 하지 않고 지연 로딩으로 냅두고, 대신 `default_batch_fetch_size` 옵션을 적용한다
		// `default_batch_fetch_size` 옵션을 통해 컬렉션 형태의 데이터를 `in` 쿼리를 통해 미리 가져온다
		// 그리고 ToOne 관계에만 fetch join을 썼기 때문에 페이징도 가능하다!
		return em.createQuery("select o from Order o" +
							  " join fetch o.member m" +
							  " join fetch o.delivery d", Order.class)
				 .setFirstResult(offset)
				 .setMaxResults(limit)
				 .getResultList();
	}

	public List<Order> findAllWithItem() {
		// distinct 를 붙이지 않으면 중복 데이터가 생긴다.
		return em.createQuery("select distinct o from Order o" +
							  " join fetch o.member m" +
							  " join fetch o.delivery d" +
							  " join fetch o.orderItems oi" +
							  " join fetch oi.item i", Order.class)
				 .getResultList();
	}

	// Repository는 가급적이면 순수 엔티티를 조회하는데만 사용하자. 성능최적화를 위해 DTO 조회가 필요할 경우 따로 Repository를 만든다.
//	public List<OrderSimpleQueryDto> findOrderDtos() {
//		return em.createQuery(
//					 "select new jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
//					 " from Order o" +
//					 " join o.member m" +
//					 " join o.delivery d", OrderSimpleQueryDto.class)
//				 .getResultList();
//	}
}
