package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //@JsonIgnore // 엔티티에 프레젠테이션 계층 로직이 들어가선 안됨.
    @OneToMany(mappedBy = "member") // Order에 있는 member필드에 의해 매핑 된 것이야
    private List<Order> orders = new ArrayList<>();
}
