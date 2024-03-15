package com.ssafy.triptogether.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.triptogether.global.domain.BaseEntity;
import com.ssafy.triptogether.plan.domain.Plan;
import com.ssafy.triptogether.review.domain.Review;
import com.ssafy.triptogether.syncaccount.domain.SyncAccount;
import com.ssafy.triptogether.tripaccount.domain.TripAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "bank_uuid")
    private String uuid;

    @Column(name = "pin_num")
    private String pinNum;

    @NotBlank
    @Column(name = "nickname")
    private String nickname;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NotBlank
    @Column(name = "birth")
    private Date birth;

    @Column(name = "image_url", length = 3000)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "rate")
    private Double rate;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TripAccount> tripAccounts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SyncAccount> syncAccounts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Plan> plans = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<MemberFlashMob> memberFlashMobs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberSettlement> memberSettlements = new ArrayList<>();

    @Builder
    public Member(String uuid, String nickname, Gender gender, Date birth) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
    }
}
