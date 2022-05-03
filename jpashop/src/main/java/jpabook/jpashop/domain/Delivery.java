package jpabook.jpashop.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Delivery extends BaseEntity {

	@Id @GeneratedValue
	private Long id;

	@Embedded
	private Address address;
	private DeliveryStatus status;

	// Delivery(FK)-Order 1대1 양방향으로 하고 싶은 경우!
	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
	private Order order;
}
