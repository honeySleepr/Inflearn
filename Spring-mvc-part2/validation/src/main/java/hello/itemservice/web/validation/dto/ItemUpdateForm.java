package hello.itemservice.web.validation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemUpdateForm {

	@NotNull
	private Long id;

	@NotBlank
	private String itemName;

	@NotNull
	@Range(min = 1_000, max = 1_000_000)
	private Integer price;

	/* 수정 시에는 등록과 다르게 수량 자유롭게 변경 가능 */
	private Integer quantity;
}
