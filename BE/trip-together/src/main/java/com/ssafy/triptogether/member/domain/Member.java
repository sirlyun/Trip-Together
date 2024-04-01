package com.ssafy.triptogether.member.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.triptogether.flashmob.domain.MemberFlashMob;
import com.ssafy.triptogether.flashmob.domain.MemberSettlement;
import com.ssafy.triptogether.global.domain.BaseEntity;
import com.ssafy.triptogether.member.data.request.ProfileUpdateRequest;
import com.ssafy.triptogether.plan.domain.Plan;
import com.ssafy.triptogether.review.domain.Review;
import com.ssafy.triptogether.syncaccount.domain.SyncAccount;
import com.ssafy.triptogether.tripaccount.domain.TripAccount;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(name = "nickname")
	private String nickname;

	@NotBlank
	@Column(name = "username")
	private String username;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@NotNull
	@Column(name = "birth")
	private LocalDate birth;

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
	public Member(String username, String uuid, String nickname, Gender gender, LocalDate birth) {
		this.username = username;
		this.uuid = uuid;
		this.nickname = nickname;
		this.gender = gender;
		this.birth = birth;
	}

	public void update(ProfileUpdateRequest profileUpdateRequest) {
		this.imageUrl = profileUpdateRequest.imageUrl();
		this.nickname = profileUpdateRequest.nickname();
		this.description = profileUpdateRequest.description();
	}

	public void savePin(String pinNum) {
		this.pinNum = pinNum;
	}
}
