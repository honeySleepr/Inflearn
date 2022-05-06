package jpabook.jpashop.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

	@Autowired
	private EntityManager em;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception {
		// given
		Member member = createMember();

		Item item = createBook("JPA 101", 10000, 10);

		// when
		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		// then
		Order findOrder = orderRepository.findOne(orderId);

		assertEquals("상품 주문시 상태가 ORDER로 적용되어야한다", OrderStatus.ORDER, findOrder.getStatus());
		assertEquals("주문한 상품의 종류 수가 일치해야한다", 1, findOrder.getOrderItems().size());
		assertEquals("주문 가격이 수량*가격으로 계산되어야한다", 20000, findOrder.getTotalPrice());
		assertEquals("주문한 수량만큼 재고가 줄어야한다", 8, item.getStockQuantity());
	}


	@Test(expected = NotEnoughStockException.class)
	public void 상품주문_재고수량초과() throws Exception {
		// given
		Member member = createMember();
		Item book = createBook("JPA 101", 10000, 10);

		// when
		int orderCount = 11;
		orderService.order(member.getId(), book.getId(), orderCount);

		// then
		fail("재고 수량 부족 예외가 발생해야한다");
	}

	@Test
	public void 주문취소() throws Exception {
		// given
		Member member = createMember();
		Item book = createBook("책", 10000, 10);
		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

		// then
		Order findOrder = orderRepository.findOne(orderId);

		assertEquals("주문 취소시 주문의 상태가 CANCEL로 변경되어야한다",
			OrderStatus.CANCEL, findOrder.getStatus());
		assertEquals("주문이 취소되면 재고수량이 원상복구 되어야한다",
			10, book.getStockQuantity());

	}

	private Item createBook(String name, int price, int stockQuantity) {
		Item item = new Book();
		item.setName(name);
		item.setPrice(price);
		item.setStockQuantity(stockQuantity);
		em.persist(item);
		return item;
	}

	private Member createMember() {
		Member member = new Member();
		em.persist(member);
		return member;
	}
}
