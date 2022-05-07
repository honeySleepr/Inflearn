package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;

	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	// Entity를 노출하지 않고 API에 맞는 별도의 DTO를 만든다
	//	- Entity가 변경되어도 API 스펙이 변경되지 않는다
	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
		Member member = new Member();
		member.setName(request.getName());

		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	// 이렇게 Entity가 노출되게 되면, Entity 변경 시 API도 바뀌어버리므로 좋지 않다
	@GetMapping("/api/v1/members")
	public List<Member> membersV1() {
		return memberService.findMembers();
	}

	// List로 바로 내보내면 유연성이 떨어지기(count 같은걸 추가할 수 없음) 때문에 한번 더 감싸준다
	@GetMapping("/api/v2/members")
	public Result memberV2() {
		List<Member> findMembers = memberService.findMembers();
		List<MemberDto> collect = findMembers.stream()
			.map(m -> new MemberDto(m.getName()))
			.collect(Collectors.toList());

		return new Result(collect.size(), collect);
	}

	@Data
	@AllArgsConstructor
	static class MemberDto {

		private String name;
	}

	@Data
	@AllArgsConstructor
	static class Result<T> {

		private int count;
		private T data;
	}


	@PutMapping("/api/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(
		@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {

		memberService.update(id, request.getName());
		// update에서 Member를 반환해도 되지만, 그러면 업데이트를 하면서 동시에 (조회를 위한) 쿼리도 같이 하기 때문에
		// 유지보수 측면에서 분리해주는 게 좋다
		Member findMember = memberService.findOne(id);
		return new UpdateMemberResponse(findMember.getId(), findMember.getName());
	}

	@Data
	static class UpdateMemberRequest {

		private String name;
	}

	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {

		private Long id;
		private String name;
	}

	@Data
	static class CreateMemberRequest {

		private String name;

	}

	@Data
	static class CreateMemberResponse {

		private Long id;

		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}

}
