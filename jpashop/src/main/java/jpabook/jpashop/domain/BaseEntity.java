package jpabook.jpashop.domain;


import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;

@MappedSuperclass // 공통 속성들을 한곳에서 관리하고 싶을 때 쓴다. 테이블은 따로 만들어지지 않는다
public abstract class BaseEntity {

	private String createdBy;
	private LocalDateTime createdDate;
	private String lastModifiedBy;
	private LocalDateTime lastModifiedDated;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public LocalDateTime getLastModifiedDated() {
		return lastModifiedDated;
	}

	public void setLastModifiedDated(LocalDateTime lastModifiedDated) {
		this.lastModifiedDated = lastModifiedDated;
	}
}
