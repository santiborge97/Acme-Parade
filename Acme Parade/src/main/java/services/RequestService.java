
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestRepository;
import security.Authority;
import domain.Member;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed Repository ------------------------
	@Autowired
	private RequestRepository	requestRepository;

	// Suporting services ------------------------

	@Autowired
	private MemberService		memberService;

	@Autowired
	private Validator			validator;

	@Autowired
	private ParadeService		paradeService;


	// CRUD Methods ------------------------

	public Request create() {
		final Member member = this.memberService.findByPrincipal();
		Assert.notNull(member);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
		Assert.isTrue(member.getUserAccount().getAuthorities().contains(authority));

		final Request result = new Request();

		result.setMember(member);
		result.setStatus("PENDING");

		return result;
	}

	public Request save(final Request request) {
		Assert.notNull(request);
		Request result;
		result = this.requestRepository.save(request);
		return result;
	}

	public void delete(final Request request) {
		Assert.notNull(request);
		final Member member = this.memberService.findByPrincipal();
		Assert.notNull(member);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
		Assert.isTrue(member.getUserAccount().getAuthorities().contains(authority));

		Assert.isTrue(request.getStatus().equals("PENDING"));

		this.requestRepository.delete(request);
	}

	public void deleteAll(final int actorId) {

		final Collection<Request> requests = this.requestRepository.findRequestsByMemberId(actorId);

		if (!requests.isEmpty())
			for (final Request r : requests)
				this.requestRepository.delete(r);

	}
	public Request findOne(final int requestId) {
		final Request result = this.requestRepository.findOne(requestId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Request> findAll() {
		final Collection<Request> requests = this.requestRepository.findAll();

		Assert.notNull(requests);

		return requests;
	}

	public Request reconstruct(final Request request, final BindingResult binding) {
		Request result;

		result = this.findOne(request.getId());
		result.setComment(request.getComment());
		result.setColumnNumber(request.getColumnNumber());
		result.setRowNumber(request.getRowNumber());

		this.validator.validate(result, binding);

		return result;
	}

	public Request reconstructAccept(final Request request, final BindingResult binding) {
		final Request resultBBDD = this.findOne(request.getId());

		request.setComment(resultBBDD.getComment());
		request.setMember(resultBBDD.getMember());
		request.setParade(resultBBDD.getParade());
		request.setStatus(resultBBDD.getStatus());
		request.setVersion(resultBBDD.getVersion());

		this.validator.validate(request, binding);

		return request;
	}

	public void flush() {
		this.requestRepository.flush();
	}

	// Other business methods -----------------------

	public Collection<Request> findRequestsByMemberId(final int memberId) {
		final Collection<Request> requests = this.requestRepository.findRequestsByMemberId(memberId);
		Assert.notNull(requests);
		return requests;
	}

	public Collection<Request> findPendingRequestsByBrotherhoodId(final int brotherhoodId) {
		final Collection<Request> requests = this.requestRepository.findPendingRequestsByBrotherhoodId(brotherhoodId);
		Assert.notNull(requests);
		return requests;
	}

	public Collection<Request> findFinalRequestsByBrotherhoodId(final int brotherhoodId) {
		final Collection<Request> requests = this.requestRepository.findFinalRequestsByBrotherhoodId(brotherhoodId);
		Assert.notNull(requests);
		return requests;
	}

	public Request suggestNextRow(final int paradeId, final Request request) {

		final Integer maxColumn = this.paradeService.findOne(paradeId).getMaxColumn();
		final Integer maxRow = this.paradeService.findOne(paradeId).getMaxRow();
		Integer i = 0;
		Integer j = 0;
		Integer avaliable = 1;

		while (i < maxRow) {

			j = 0;

			while (j < maxColumn) {

				avaliable = this.requestRepository.nextFreePosition(paradeId, i, j);

				if (avaliable == 0)
					break;

				j++;

			}

			if (avaliable == 0)
				break;

			i++;
		}

		request.setColumnNumber(j);
		request.setRowNumber(i);

		return request;
	}
	public Collection<Double> ratiosRequest() {

		final Collection<Double> res = this.requestRepository.ratiosRequest();

		return res;
	}

	public Collection<Request> requestPerParadeId(final int paradeId) {

		final Collection<Request> res = this.requestRepository.requestPerParadeId(paradeId);

		return res;
	}

	public Collection<Request> paradeApproved() {
		final Collection<Request> res = this.requestRepository.paradeApproved();

		return res;
	}

	public Collection<Request> paradeApproved(final int paradeId) {
		final Collection<Request> res = this.requestRepository.paradeApproved(paradeId);

		return res;
	}

	public Collection<Request> paradePending(final int paradeId) {
		final Collection<Request> res = this.requestRepository.paradePending(paradeId);

		return res;
	}

	public Collection<Request> paradeRejected(final int paradeId) {
		final Collection<Request> res = this.requestRepository.paradeRejected(paradeId);

		return res;
	}

	public String ratioParadeAccepted(final int paradeId) {

		String res;

		final Collection<Request> total = this.requestPerParadeId(paradeId);
		final Collection<Request> accepted = this.paradeApproved(paradeId);

		if (total.isEmpty())
			res = "N/A";
		else {
			Double ratio = 0.0;
			if (accepted.isEmpty())
				res = String.valueOf(ratio);
			else {
				ratio = (accepted.size() * 1.0) / total.size();
				res = String.valueOf(ratio);
			}

		}

		return res;

	}

	public String ratioParadePending(final int paradeId) {

		String res;

		final Collection<Request> total = this.requestPerParadeId(paradeId);
		final Collection<Request> pending = this.paradePending(paradeId);

		if (total.isEmpty())
			res = "N/A";
		else {
			Double ratio = 0.0;
			if (pending.isEmpty())
				res = String.valueOf(ratio);
			else {
				ratio = (pending.size() * 1.0) / total.size();
				res = String.valueOf(ratio);
			}

		}

		return res;

	}

	public String ratioParadeRejected(final int paradeId) {

		String res;

		final Collection<Request> total = this.requestPerParadeId(paradeId);
		final Collection<Request> rejected = this.paradeRejected(paradeId);

		if (total.isEmpty())
			res = "N/A";
		else {
			Double ratio = 0.0;
			if (rejected.isEmpty())
				res = String.valueOf(ratio);
			else {
				ratio = (rejected.size() * 1.0) / total.size();
				res = String.valueOf(ratio);
			}

		}

		return res;

	}

	public Boolean hasRequestIn(final Integer col, final Integer row, final Integer paradeId) {
		return this.requestRepository.requestIn(col, row, paradeId).size() > 0 ? true : false;
	}

	public Boolean hasAcceptedOrPendingRequestsOfMemberIn(final Integer memberId, final Integer paradeId) {
		return this.requestRepository.findAcceptedOrPendingRequestsOfMemberIn(memberId, paradeId).size() > 0 ? true : false;
	}
}
