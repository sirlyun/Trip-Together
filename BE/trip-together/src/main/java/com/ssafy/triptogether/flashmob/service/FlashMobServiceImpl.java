package com.ssafy.triptogether.flashmob.service;

import static com.ssafy.triptogether.global.exception.response.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.triptogether.auth.utils.SecurityMember;
import com.ssafy.triptogether.auth.validator.flashmobmember.FlashMobMemberVerify;
import com.ssafy.triptogether.flashmob.data.request.ApplyFlashmobRequest;
import com.ssafy.triptogether.flashmob.data.request.SettlementSaveAttendeesDetail;
import com.ssafy.triptogether.flashmob.data.request.SettlementSaveRequest;
import com.ssafy.triptogether.flashmob.data.response.AttendeeReceiptsResponse;
import com.ssafy.triptogether.flashmob.data.response.AttendeesStatusResponse;
import com.ssafy.triptogether.flashmob.data.response.AttendingFlashmobFindResponse;
import com.ssafy.triptogether.flashmob.data.response.AttendingFlashmobListFindResponse;
import com.ssafy.triptogether.flashmob.data.response.SettlementsLoadResponse;
import com.ssafy.triptogether.flashmob.domain.FlashMob;
import com.ssafy.triptogether.flashmob.domain.MemberFlashMob;
import com.ssafy.triptogether.flashmob.domain.MemberSettlement;
import com.ssafy.triptogether.flashmob.domain.ParticipantSettlement;
import com.ssafy.triptogether.flashmob.domain.Settlement;
import com.ssafy.triptogether.flashmob.domain.document.Receipt;
import com.ssafy.triptogether.flashmob.domain.document.ReceiptHistory;
import com.ssafy.triptogether.flashmob.repository.FlashMobRepository;
import com.ssafy.triptogether.flashmob.repository.MemberFlashMobRepository;
import com.ssafy.triptogether.flashmob.repository.ParticipantSettlementRepository;
import com.ssafy.triptogether.flashmob.repository.ReceiptRepository;
import com.ssafy.triptogether.flashmob.repository.RequesterSettlementRepository;
import com.ssafy.triptogether.flashmob.repository.SettlementRepository;
import com.ssafy.triptogether.flashmob.utils.FlashMobUtils;
import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
import com.ssafy.triptogether.global.exception.exceptions.category.ForbiddenException;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.domain.RoomStatus;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.utils.MemberFlashmobUtils;
import com.ssafy.triptogether.member.utils.MemberUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FlashMobServiceImpl implements FlashMobSaveService, FlashMobLoadService {

	private final FlashMobRepository flashMobRepository;
	private final MemberFlashMobRepository memberFlashMobRepository;
	private final MemberRepository memberRepository;
	private final SettlementRepository settlementRepository;
	private final RequesterSettlementRepository requesterSettlementRepository;
	private final ParticipantSettlementRepository participantSettlementRepository;
	private final ReceiptRepository receiptRepository;

	@Transactional
	@Override
	public void sendAttendanceRequest(long flashmobId, long memberId) {
		// find member & flashmob
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);
		FlashMob flashMob = flashMobRepository.findById(flashmobId)
			.orElseThrow(() -> new NotFoundException("SendAttendanceRequest", UNDEFINED_FLASHMOB));

		// create member flashmob & save
		MemberFlashMob memberFlashMob = MemberFlashMob.builder()
			.isMaster(false)
			.roomStatus(RoomStatus.WAIT)
			.member(member)
			.flashMob(flashMob)
			.build();
		memberFlashMobRepository.save(memberFlashMob);

		// send chat message
		// TODO: 해당 채팅방에 참가요청에 대한 채팅 메시지 전송
	}

	@Transactional
	@Override
	public void checkDeniedFlashmob(long flashmobId, long memberId) {
		// find member flashmob
		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(memberFlashMobRepository,
			flashmobId, memberId);

		// check room status
		memberFlashMob.checkDenial();
	}

	@Override
	public void cancelFlashmob(long flashmobId, long memberId) {
		// find member flashmob
		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(memberFlashMobRepository,
			flashmobId, memberId);

		// cancel attendance request
		memberFlashMobRepository.delete(memberFlashMob);
	}

	// TODO: 해당 사용자에게 메시지 큐 연분
	@Transactional
	@Override
	public boolean applyFlashmob(
		long flashmobId, long memberId, ApplyFlashmobRequest applyFlashmobRequest, long masterId) {
		FlashMob flashMob = FlashMobUtils.findByFlashmobId(flashMobRepository, flashmobId);
		boolean isMaster = memberFlashMobRepository.isMaster(flashmobId, masterId);
		if (!isMaster) {
			throw new ForbiddenException("applyFlashmob", MEMBER_NOT_MASTER, masterId);
		}

		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(memberFlashMobRepository,
			flashmobId, memberId);
		if (applyFlashmobRequest.status().equals(RoomStatus.ATTEND)) {
			memberFlashMob.applyAcceptance();
			return true; // 수락되었을 시에만 true 반환
		} else if (applyFlashmobRequest.status().equals(RoomStatus.REFUSE_UNCHECK)) {
			memberFlashMob.applyDenial();
		} else {
			throw new BadRequestException("applyFlashmob", BAD_STATUS_REQUEST, applyFlashmobRequest.status());
		}
		return false;
	}

	@Transactional
	@Override
	public void exitFlashmob(SecurityMember securityMember, long flashmobId) {
		long memberCnt = memberFlashMobRepository.countMemberFlashMobsByFlashMob_Id(flashmobId);
		if (memberCnt == 1L) {
			flashMobRepository.deleteById(flashmobId);
			return;
		}

		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(
			memberFlashMobRepository, flashmobId, securityMember.getId());
		if (memberFlashMob.getIsMaster()) {
			MemberFlashMob nextMaster = MemberFlashmobUtils.findByFlashmobIdNotInMemberId(
				memberFlashMobRepository, flashmobId, securityMember.getId());
			// 다른 멤버 찾아서 방장으로 바꿈
			nextMaster.memberToMaster();
		}
		// 멤버를 repo.delete
		memberFlashMobRepository.delete(memberFlashMob);
	}

	/**
	 * 정산 요청
	 * @param memberId 요청자 member_id
	 * @param flashmobId 발생한 플래시몹 flashmob_id
	 * @param settlementSaveRequest 정산 요청 내용
	 */
	@FlashMobMemberVerify
	@Transactional
	@Override
	public void settlementSave(long memberId, long flashmobId, SettlementSaveRequest settlementSaveRequest) {
		Member requester = MemberUtils.findByMemberId(memberRepository, memberId);
		FlashMob flashMob = FlashMobUtils.findByFlashmobId(flashMobRepository, flashmobId);
		Settlement settlement = makeSettlement(settlementSaveRequest, flashMob);
		// RequesterSettlement requesterSettlement = (RequesterSettlement)RequesterSettlement.builder()
		// 	.member(requester)
		// 	.settlement(settlement)
		// 	.build();
		// requesterSettlementRepository.save(requesterSettlement);

		settlementSaveRequest.attendeesDetails()
			.forEach(attendeesDetail -> {
				Member sender = MemberUtils.findByMemberId(memberRepository, attendeesDetail.memberId());
				ParticipantSettlement participantSettlement = ParticipantSettlement.builder()
					.price(attendeesDetail.memberPrice())
					.hasSent(false)
					.member(sender)
					.settlement(settlement)
					.build();
				participantSettlementRepository.save(participantSettlement);
				makeReceipt(attendeesDetail, participantSettlement);
			});
	}

	private void makeReceipt(SettlementSaveAttendeesDetail attendeesDetail, MemberSettlement participantSettlement) {
		List<ReceiptHistory> receiptHistories = attendeesDetail.receiptDetails()
			.stream().map(attendeesReceiptDetail ->
				ReceiptHistory.builder()
					.price(attendeesReceiptDetail.price())
					.businessName(attendeesReceiptDetail.businessName())
					.createdAt(attendeesReceiptDetail.createdAt())
					.build()
			).toList();
		Receipt receipt = Receipt.builder()
			.memberSettlementId(participantSettlement.getId())
			.receiptHistories(receiptHistories)
			.build();
		receiptRepository.save(receipt);
	}

	private Settlement makeSettlement(SettlementSaveRequest settlementSaveRequest, FlashMob flashMob) {
		Settlement settlement = Settlement.builder()
			.currencyCode(settlementSaveRequest.currencyCode())
			.attendanceCount(settlementSaveRequest.attendeesCount())
			.totalPrice(settlementSaveRequest.totalPrice())
			.flashMob(flashMob)
			.build();
		settlementRepository.save(settlement);
		return settlement;
	}

	@Override
	public AttendingFlashmobListFindResponse findAttendingFlashmobList(long memberId) {
		// find attending flashmobs
		List<AttendingFlashmobFindResponse> elements = flashMobRepository.findAllAttendingFlashmobElementsByMemberId(
			memberId);

		// create response & return
		return AttendingFlashmobListFindResponse.builder().elements(elements).build();
	}

	/**
	 * 플래시몹 내 정산 목록 조회
	 * @param memberId 요청자 member_id
	 * @param flashmobId 플래시몹 flashmob_id
	 * @return 정산 목록
	 */
	@FlashMobMemberVerify
	@Override
	public SettlementsLoadResponse settlementsLoad(long memberId, long flashmobId) {
		// List<MemberSettlement> requesterSettlements = requesterSettlementRepository.findByMemberId(memberId);
		// List<Settlement> settlements = settlementRepository.findByFlashMobId(flashmobId);
		// List<SettlementsLoadDetail> settlementsLoadDetails = settlements.stream()
		// 	.filter(settlement ->
		// 		settlement.getRequesterId().equals(memberId) ||
		// 			settlement.getMemberSettlements().stream().anyMatch(memberSettlement ->
		// 				memberSettlement.getMember().getId().equals(memberId)))
		// 	.map(settlement -> SettlementsLoadDetail.builder()
		// 		.settlementId(settlement.getId())
		// 		.currencyCode(settlement.getCurrencyCode())
		// 		.isDone(settlement.getIsDone())
		// 		.isReceiver(settlement.getRequesterId().equals(memberId))
		// 		.receiverId(settlement.getRequesterId())
		// 		.totalPrice(settlement.getTotalPrice())
		// 		.receiverImageUrl(
		// 			MemberUtils.findByMemberId(memberRepository, settlement.getRequesterId()).getImageUrl())
		// 		.receiverNickname(
		// 			MemberUtils.findByMemberId(memberRepository, settlement.getRequesterId()).getNickname())
		// 		.build()
		// 	).toList();
		// return SettlementsLoadResponse.builder()
		// 	.settlementsLoadDetails(settlementsLoadDetails)
		// 	.build();
		return null;
	}

	@FlashMobMemberVerify
	@Override
	public AttendeeReceiptsResponse receiptsLoad(long memberId, long flashmobId, long settlementId) {
		// MemberSettlement memberSettlement = FlashMobUtils.findByMemberIdAndSettlementId(
		// 	requesterSettlementRepository, memberId, settlementId);
		// Receipt receipt = receiptRepository.findById(memberSettlement.getId())
		// 	.orElseThrow(
		// 		() -> new NotFoundException("ReceiptsLoad", RECEIPT_NOT_FOUND)
		// 	);
		// List<AttendeesReceiptDetail> attendeesReceiptDetails = receipt.getReceiptHistories()
		// 	.stream()
		// 	.map(
		// 		receiptHistory -> AttendeesReceiptDetail.builder()
		// 			.price(receiptHistory.price())
		// 			.businessName(receiptHistory.businessName())
		// 			.createdAt(receiptHistory.createdAt())
		// 			.build()
		// 	).toList();
		// return AttendeeReceiptsResponse.builder()
		// 	.price(memberSettlement.getPrice())
		// 	.attendeesReceiptDetails(attendeesReceiptDetails)
		// 	.build();
		return null;
	}

	@FlashMobMemberVerify
	@Override
	public AttendeesStatusResponse attendeesStatusLoad(long memberId, long flashmobId, long settlementId) {
		// Settlement settlement = settlementRepository.findById(settlementId)
		// 	.orElseThrow(
		// 		() -> new NotFoundException("AttendeesStatusLoad", SETTLEMENT_NOT_FOUND)
		// 	);
		// if (!settlement.getRequesterId().equals(memberId)) {
		// 	throw new ForbiddenException("AttendeesStatusLoad", FORBIDDEN_ACCESS_MEMBER);
		// }
		// List<AttendeesStatusDetail> attendeesStatusDetails = requesterSettlementRepository.memberSettlementStatusLoad(
		// 	settlementId);
		// return AttendeesStatusResponse.builder()
		// 	.attendeesStatusDetails(attendeesStatusDetails)
		// 	.build();
		return null;
	}
}
