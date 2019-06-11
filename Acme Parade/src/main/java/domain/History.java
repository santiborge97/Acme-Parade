
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class History extends DomainEntity {

	private InceptionRecord					inceptionRecord;
	private Collection<LegalRecord>			legalRecords;
	private Collection<PeriodRecord>		periodRecords;
	private Collection<LinkRecord>			linkRecords;
	private Collection<MiscellaneousRecord>	miscellaneousRecords;
	private Brotherhood						brotherhood;


	@Valid
	@OneToOne(optional = true, cascade = CascadeType.ALL)
	public InceptionRecord getInceptionRecord() {
		return this.inceptionRecord;
	}

	public void setInceptionRecord(final InceptionRecord inceptionRecord) {
		this.inceptionRecord = inceptionRecord;
	}

	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<LegalRecord> getLegalRecords() {
		return this.legalRecords;
	}

	public void setLegalRecords(final Collection<LegalRecord> legalRecords) {
		this.legalRecords = legalRecords;
	}

	public void addLegalRecords(final LegalRecord legalRecord) {
		this.legalRecords.add(legalRecord);
	}

	public void removeLegalRecords(final LegalRecord legalRecord) {
		this.legalRecords.remove(legalRecord);
	}

	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<PeriodRecord> getPeriodRecords() {
		return this.periodRecords;
	}

	public void setPeriodRecords(final Collection<PeriodRecord> periodRecords) {
		this.periodRecords = periodRecords;
	}

	public void addPeriodRecords(final PeriodRecord periodRecord) {
		this.periodRecords.add(periodRecord);
	}

	public void removePeriodRecords(final PeriodRecord periodRecord) {
		this.periodRecords.remove(periodRecord);
	}

	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<LinkRecord> getLinkRecords() {
		return this.linkRecords;
	}

	public void setLinkRecords(final Collection<LinkRecord> linkRecords) {
		this.linkRecords = linkRecords;
	}

	public void addLinkRecords(final LinkRecord linkRecord) {
		this.linkRecords.add(linkRecord);
	}

	public void removeLinkRecords(final LinkRecord linkRecord) {
		this.periodRecords.remove(linkRecord);
	}

	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<MiscellaneousRecord> getMiscellaneousRecords() {
		return this.miscellaneousRecords;
	}

	public void setMiscellaneousRecords(final Collection<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

	public void addMiscellaneousRecords(final MiscellaneousRecord miscellaneousRecord) {
		this.miscellaneousRecords.add(miscellaneousRecord);
	}

	public void removeMiscellaneousRecords(final MiscellaneousRecord miscellaneousRecord) {
		this.miscellaneousRecords.remove(miscellaneousRecord);
	}

	@Valid
	@ManyToOne(optional = false)
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

}
