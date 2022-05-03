package jpabook.jpashop.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable // 값 타입으로 사용하겠다. 값타입끼리 비교할 일이 있을 경우 equal & hashCode 오버라이딩!
public class Address {

	private String city;
	private String street;
	@Column(length = 6) // varchar(6)으로 지정
	private String zipcode;

	public String getCity() {
		return city;
	}

	private void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	private void setStreet(String street) {
		this.street = street;
	}

	public String getZipcode() {
		return zipcode;
	}

	private void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Address address = (Address) o;
		return Objects.equals(getCity(), address.getCity()) && Objects.equals(
			getStreet(), address.getStreet()) && Objects.equals(getZipcode(),
			address.getZipcode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCity(), getStreet(), getZipcode());
	}
}
