package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PatchMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName()); //커맨드
        Member findMember = memberService.findOne(id); // 쿼리 분리
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    @GetMapping("/api/v1/members")
    public List<Member>  getMembersV1() {
        return memberService.findMembers();
    }
    @GetMapping("/api/v2/members")
    public Result getMembersV2() {
        List<Member> findMembers = memberService.findMembers();
        // 엔티티 -> DTO로 변환
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data // 껍데기 반환값
    @AllArgsConstructor
    static class Result<T> {
        private int count; // count 기능 추가
        private T data;
    }
    @Data // 이름만 반환해주는 dto
    @AllArgsConstructor
    static class MemberDto {
        private String name;
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
