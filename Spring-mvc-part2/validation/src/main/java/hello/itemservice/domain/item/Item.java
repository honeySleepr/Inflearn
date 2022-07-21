package hello.itemservice.domain.item;

import lombok.Data;

@Data
public class Item {

	//	@NotNull(groups = UpdateCheck.class) /*수정할 때만 체크*/
	private Long id;

	//	@NotBlank(message = "공백 안됨~~", groups = {UpdateCheck.class, SaveCheck.class})
	private String itemName;

	//	@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
	//	@Range(min = 1_000, max = 1_000_000)
	private Integer price;

	//	@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
	//	@Max(value = 9999, groups = SaveCheck.class) /*저장할 때만 체크*/
	private Integer quantity;

	public Item() {
	}

	public Item(String itemName, Integer price, Integer quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
}
