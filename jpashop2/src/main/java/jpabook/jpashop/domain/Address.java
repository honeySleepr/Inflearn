package jpabook.jpashop.domain;

import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

	private String city;
	private String street;
	private String zipcode;

	// JPA 스펙상 기본 생성자 필요. private은 안되지만 protected 까지는 가능
	protected Address() {
	}

	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}
}
