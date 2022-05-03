package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;
	@Column(name = "USERNAME")
	private String username;
	@Column(name = "TEAM_ID")
	private long teamId;

}
