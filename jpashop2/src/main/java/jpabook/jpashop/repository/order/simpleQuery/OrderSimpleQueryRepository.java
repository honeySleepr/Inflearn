package jpabook.jpashop.repository.order.simpleQuery;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

	private final EntityManager em;

	public List<OrderSimpleQueryDto> findOrderDtos() {
		// OrderSimpleQueryDto(o) 형태로 Order를 인자로 넣으면 엔티티로 넘어가는게 아니라 식별자로(?) 넘어가서 원하는대로 작동하지 않는다
		return em.createQuery(
					 "select new jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
					 " from Order o" +
					 " join o.member m" +
					 " join o.delivery d", OrderSimpleQueryDto.class)
				 .getResultList();
	}
}
