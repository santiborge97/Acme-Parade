
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SegmentRepository;
import domain.Parade;
import domain.Segment;
import forms.ContiguousSegmentForm;
import forms.FirstSegmentForm;

@Service
@Transactional
public class SegmentService {

	// Managed repository

	@Autowired
	private SegmentRepository	segmentRepository;

	@Autowired
	private Validator			validator;


	// Suporting services

	public Collection<Segment> findByParade(final int paradeId) {
		return this.segmentRepository.segmentsPerParade(paradeId);
	}


	@Autowired
	private ParadeService	paradeService;


	// Simple CRUD methods

	public Segment create() {

		final Segment result = new Segment();

		return result;

	}
	public Collection<Segment> findAll() {

		final Collection<Segment> segments = this.segmentRepository.findAll();

		Assert.notNull(segments);

		return segments;
	}

	public Segment findOne(final int segmentId) {

		final Segment segment = this.segmentRepository.findOne(segmentId);

		return segment;

	}

	public Segment save(final Segment segment) {
		Segment result;

		Assert.isTrue(segment.getTimeOrigin().before(segment.getTimeDestination()));

		Assert.isTrue(!segment.getTimeOrigin().before(segment.getParade().getOrganisationMoment()));
		if (segment.getId() != 0) {
			if (segment.getContiguous() != null) {

				final Segment contiguousBefore = segment.getContiguous();

				contiguousBefore.setDestination(segment.getOrigin());
				contiguousBefore.setTimeDestination(segment.getTimeOrigin());

				Assert.isTrue(contiguousBefore.getTimeOrigin().before(segment.getTimeOrigin()));
				Assert.isTrue(contiguousBefore.getTimeDestination().before(segment.getTimeDestination()));

				Assert.isTrue(contiguousBefore.getTimeOrigin().before(contiguousBefore.getTimeDestination()));

				this.segmentRepository.save(contiguousBefore);

			}

			final Segment contiguousAfter = this.segmentContiguous(segment.getId());

			if (contiguousAfter != null) {

				contiguousAfter.setOrigin(segment.getDestination());
				contiguousAfter.setTimeOrigin(segment.getTimeDestination());

				Assert.isTrue(contiguousAfter.getTimeOrigin().after(segment.getTimeOrigin()));
				Assert.isTrue(contiguousAfter.getTimeDestination().after(segment.getTimeDestination()));

				Assert.isTrue(contiguousAfter.getTimeOrigin().before(contiguousAfter.getTimeDestination()));

				this.segmentRepository.save(contiguousAfter);

			}

		}

		result = this.segmentRepository.save(segment);

		return result;
	}

	public void delete(final Segment segment) {

		Assert.isTrue(segment.getId() != 0);

		final Segment contiguousAfter = this.segmentContiguous(segment.getId());

		Assert.isTrue(contiguousAfter == null);

		this.segmentRepository.delete(segment);

	}

	public Segment reconstruct(final FirstSegmentForm segment, final BindingResult binding) {

		final Segment result = this.create();

		this.validator.validate(segment, binding);

		final Segment contiguous = this.lastSegment(segment.getParadeId());

		final Parade parade = this.paradeService.findOne(segment.getParadeId());

		result.setId(segment.getId());
		result.setVersion(segment.getVersion());
		result.setOrigin(segment.getOrigin());
		result.setDestination(segment.getDestination());
		result.setTimeOrigin(segment.getTimeOrigin());
		result.setTimeDestination(segment.getTimeDestination());
		result.setContiguous(contiguous);
		result.setParade(parade);

		return result;
	}

	public Segment reconstruct(final ContiguousSegmentForm segment, final BindingResult binding) {

		final Segment result = this.create();

		this.validator.validate(segment, binding);

		final Segment contiguous = this.lastSegment(segment.getParadeId());

		final Parade parade = this.paradeService.findOne(segment.getParadeId());

		result.setId(segment.getId());
		result.setVersion(segment.getVersion());
		result.setOrigin(contiguous.getDestination());
		result.setDestination(segment.getDestination());
		result.setTimeOrigin(contiguous.getTimeDestination());
		result.setTimeDestination(segment.getTimeDestination());
		result.setContiguous(contiguous);
		result.setParade(parade);

		return result;
	}

	public Segment reconstruct(final Segment segment, final BindingResult binding) {

		final Segment result;

		final Segment segmentBBDD = this.findOne(segment.getId());

		segment.setContiguous(segmentBBDD.getContiguous());
		segment.setParade(segmentBBDD.getParade());

		this.validator.validate(segment, binding);

		result = segment;

		return result;
	}

	public void deleteAll(final int paradeId) {

		final Collection<Segment> segments = this.segmentRepository.segmentsPerParade(paradeId);
		int i = 0;

		if (!segments.isEmpty())
			while (i < segments.size()) {

				final Segment segment = this.segmentRepository.lastSegment(paradeId);

				this.segmentRepository.delete(segment);

				i++;

			}

	}

	// Other business methods 

	public Segment lastSegment(final int paradeId) {

		final Segment result = this.segmentRepository.lastSegment(paradeId);

		return result;
	}

	public Collection<Segment> segmentsPerParade(final int paradeId) {

		final Collection<Segment> result = this.segmentRepository.segmentsPerParade(paradeId);

		return result;
	}

	public Segment segmentContiguous(final int segmentId) {

		final Segment result = this.segmentRepository.segmentContiguous(segmentId);

		return result;
	}

	public boolean exist(final int segmentId) {

		Boolean result = false;

		final Segment segment = this.segmentRepository.findOne(segmentId);

		if (segment != null)
			result = true;

		return result;
	}

	public void flush() {
		this.segmentRepository.flush();
	}

}
