package jpabook.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

	@Id @GeneratedValue
	@Column(name = "item_id")
	private Long id;

	private String name;
	private int price;
	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();

	/* 비즈니스 로직 */
	// 엔티티 안에서 처리할 수 있는 로직은 안에서 처리하는게 응집도가 높다. 도메인 주도 설계.객체지향적.성공적.

	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	public void removeStock(int quantity) {
		int stock = this.stockQuantity - quantity;
		if (stock < 0) {
			throw new NotEnoughStockException("재고가 부족합니다!!");
		}
		this.stockQuantity = stock;
	}


}
