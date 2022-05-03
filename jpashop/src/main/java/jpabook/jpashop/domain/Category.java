package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Category {

	@Id @GeneratedValue
	private Long id;

	private String name;
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private Category parent;

	// 셀프(Category 내에서) 일대다, 다대일 매핑
	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<>();

	// Item과 다대다 (실전에는 다대다는 쓰지 말고 1대다-다대1 로 바꿔서 써야한다)
	@ManyToMany
	@JoinTable(name = "CATEGORY_ITEM",
		joinColumns = @JoinColumn(name = "CATEGORY_ID"),
		inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
	private List<Item> items = new ArrayList<>();
}
