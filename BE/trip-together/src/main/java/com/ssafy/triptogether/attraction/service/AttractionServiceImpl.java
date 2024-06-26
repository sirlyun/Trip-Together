package com.ssafy.triptogether.attraction.service;

import com.ssafy.triptogether.attraction.data.request.FlashmobCreateRequest;
import com.ssafy.triptogether.attraction.data.request.FlashmobUpdateRequest;
import com.ssafy.triptogether.attraction.data.response.*;
import com.ssafy.triptogether.attraction.domain.Attraction;
import com.ssafy.triptogether.attraction.domain.AttractionImage;
import com.ssafy.triptogether.attraction.repository.AttractionRepository;
import com.ssafy.triptogether.attraction.repository.RegionRepository;
import com.ssafy.triptogether.attraction.utils.AttractionUtils;
import com.ssafy.triptogether.flashmob.domain.FlashMob;
import com.ssafy.triptogether.flashmob.domain.MemberFlashMob;
import com.ssafy.triptogether.flashmob.repository.FlashMobRepository;
import com.ssafy.triptogether.flashmob.repository.MemberFlashMobRepository;
import com.ssafy.triptogether.flashmob.utils.FlashMobUtils;
import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
import com.ssafy.triptogether.global.utils.distance.MysqlNativeSqlCreator;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.domain.RoomStatus;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.utils.MemberUtils;
import com.ssafy.triptogether.plan.data.response.ReviewResponse;
import com.ssafy.triptogether.review.repository.ReviewRepository;
import com.ssafy.triptogether.review.utils.ReviewUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.triptogether.global.exception.response.ErrorCode.BLACKLIST_MEMBER;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AttractionServiceImpl implements AttractionSaveService, AttractionLoadService {
	private final AttractionRepository attractionRepository;
	private final ReviewRepository reviewRepository;
	private final FlashMobRepository flashMobRepository;
	private final RegionRepository regionRepository;
	private final MemberRepository memberRepository;
	private final MemberFlashMobRepository memberFlashMobRepository;

	@Override
	public AttractionDetailFindResponse findAttractionDetail(long attractionId) {
		// find attraction & reviews
		Attraction attraction = AttractionUtils.findByAttractionId(attractionRepository, attractionId);
		List<ReviewResponse> reviewResponses = ReviewUtils.findAllByAttractionId(reviewRepository, attractionId);

		// set attraction detail & return
		return AttractionDetailFindResponse.builder()
			.attractionId(attraction.getId())
			.avgPrice(attraction.getAvgPrice())
			.startAt(attraction.getStartAt())
			.endAt(attraction.getEndAt())
			.attractionThumbnailImageUrl(attraction.getThumbnailImageUrl())
			.attractionImageUrls(attraction.getAttractionImages().stream().map(AttractionImage::getImageUrl).toList())
			.attractionName(attraction.getName())
			.attractionAddress(attraction.getAddress())
			.avgRating(attraction.getAvgRating())
			.latitude(attraction.getLatitude())
			.longitude(attraction.getLongitude())
			.reviews(reviewResponses)
			.build();
	}

	@Override
	public FlashmobListFindResponse findFlashmobList(long attractionId, long memberId) {
		// find all flashmob responses
		List<FlashmobElementFindResponse> elements = FlashMobUtils.findAllFlashmobElementsByAttractionId(
			flashMobRepository, attractionId, memberId);

		// create response & return
		return FlashmobListFindResponse.builder().elements(elements).build();
	}

	/**
	 * 검색 쿼리 기반 도시 목록 조회
	 * @param name 검색 요청한 도시 이름
	 * @return 해당되는 도시 목록
	 */
	@Override
	public RegionsLoadResponse regionsLoad(String name) {
		if (name == null) {
			return RegionsLoadResponse.builder()
				.build();
		}
		List<RegionLoadDetail> regionsByName = regionRepository.findRegionsByName(name);
		return RegionsLoadResponse.builder()
			.regionLoadDetails(regionsByName)
			.build();
	}

	@Transactional
	@Override
	public FlashmobUpdateResponse updateFlashmob(long flashmobId, FlashmobUpdateRequest flashmobUpdateRequest) {
		FlashMob flashMob = FlashMobUtils.findByFlashmobId(flashMobRepository, flashmobId);
		flashMob.update(flashmobUpdateRequest);
		return FlashmobUpdateResponse.builder().flashmobId(flashmobId).build();
	}

	// TODO: 채팅방(소켓) 생성
	@Transactional
	@Override
	public long createFlashmob(Long memberId, long attractionId, FlashmobCreateRequest flashmobCreateRequest) {
		Attraction attraction = AttractionUtils.findByAttractionId(attractionRepository, attractionId);
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);

		// blacklist prevention
		if (member.getReportCount() >= 5) {
			throw new BadRequestException("sendAttendanceRequest", BLACKLIST_MEMBER);
		}

		FlashMob flashMob = FlashMob.builder()
			.attraction(attraction)
			.title(flashmobCreateRequest.title())
			.startAt(flashmobCreateRequest.startTime())
			.maxMemberCount(flashmobCreateRequest.maxUsers())
			.build();
		flashMobRepository.save(flashMob);

		MemberFlashMob memberFlashMob = MemberFlashMob.builder()
			.flashMob(flashMob)
			.member(member)
			.isMaster(true)
			.roomStatus(RoomStatus.ATTEND)
			.build();
		memberFlashMobRepository.save(memberFlashMob);

		return flashMob.getId();
	}

	@Override
	public List<AttractionListItemResponseWD> findAttractionsClick(
		double latitude, double longitude, double latitudeDelta, double longitudeDelta, String category) {
		double distance = new MysqlNativeSqlCreator().getDistance(
			0,
			0,
			latitudeDelta / 2,
			longitudeDelta / 2
		);
		return attractionRepository.findAttractionClick(
			latitude,
			longitude,
			distance,
			category
		);
	}

	@Override
	public List<AttractionListItemResponse> findAttractionsSearch(
		double latitude, double longitude, String keyword) {
		return attractionRepository.findAttractionSearch(
			latitude,
			longitude,
			keyword
		);
	}

	@Override
	public AttractionFlashmobListFindResponse findAttractionFlashmobList(
		long memberId, double latitude, double longitude, double latitudeDelta, double longitudeDelta
	) {
		double distance = new MysqlNativeSqlCreator().getDistance(0, 0, latitudeDelta / 2, longitudeDelta / 2);
		List<AttractionFlashmobListItemResponse> elements = attractionRepository.findAllAttractionFlashmobByConditions(
			latitude, longitude, distance);
		return AttractionFlashmobListFindResponse.builder().elements(elements).build();
	}
}
