package es.udc.ws.app.model.inscription;

import java.time.LocalDateTime;

public class Inscription{

	private Long inscriptionId;
	private String userEmail;
	private Long raceId;
	private String creditCardNumber;
	private int dorsalNumber;
	private LocalDateTime inscriptionDate;
	private Boolean collected;

	public Inscription(String userEmail, Long raceId, String creditCardNumber, int dorsalNumber, Boolean collected) {
		this.userEmail = userEmail;
		this.raceId = raceId;
		this.creditCardNumber = creditCardNumber;
		this.dorsalNumber = dorsalNumber;
		this.collected = collected;
	}

	public Inscription(Long inscriptionId, String userEmail, Long raceId, String creditCardNumber, int dorsalNumber, Boolean collected) {
		this(userEmail, raceId, creditCardNumber, dorsalNumber, collected);
		this.inscriptionId = inscriptionId;
	}

	/* Constructor for inscribeRace. */
	public Inscription(String userEmail, Long raceId, String creditCardNumber, int dorsalNumber, LocalDateTime inscriptionDate,
					   Boolean collected) {
		this(userEmail, raceId, creditCardNumber, dorsalNumber, collected);
		this.inscriptionDate = (inscriptionDate != null) ? inscriptionDate.withNano(0) : null;
	}

	public Inscription(Long inscriptionId,String userEmail, Long raceId, String creditCardNumber, int dorsalNumber, LocalDateTime inscriptionDate,
			Boolean collected) {
		this(inscriptionId, userEmail, raceId, creditCardNumber, dorsalNumber, collected);
		this.inscriptionDate = (inscriptionDate != null) ? inscriptionDate.withNano(0) : null;
	}

	public Inscription(Long inscriptionId, Long raceId, int dorsalNumber, Boolean collected) {
		this.inscriptionId = inscriptionId;
		this.raceId = raceId;
		this.dorsalNumber = dorsalNumber;
		this.collected = collected;
	}

	public Long getInscriptionId() {
		return inscriptionId;
	}

	public void setInscriptionId(Long inscriptionId) {
		this.inscriptionId = inscriptionId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getRaceId() {
		return raceId;
	}

	public void setRaceId(Long raceId) {
		this.raceId = raceId;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public int getDorsalNumber() {
		return dorsalNumber;
	}

	public void setDorsalNumber(int dorsalNumber) {
		this.dorsalNumber = dorsalNumber;
	}

	public Boolean getCollected() {
		return collected;
	}

	public void setCollected(Boolean collected) {
		this.collected = collected;
	}

	public LocalDateTime getInscriptionDate() {
		return inscriptionDate;
	}

	public void setCreationDate(LocalDateTime inscriptionDate) {
		this.inscriptionDate = (inscriptionDate != null) ? inscriptionDate.withNano(0) : null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collected == null) ? 0 : collected.hashCode());
		result = prime * result + dorsalNumber;
		result = prime * result + ((inscriptionDate == null) ? 0 : inscriptionDate.hashCode());
		result = prime * result + ((inscriptionId == null) ? 0 : inscriptionId.hashCode());
		result = prime * result + ((raceId == null) ? 0 : raceId.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inscription other = (Inscription) obj;
		if (collected == null) {
			if (other.collected != null)
				return false;
		} else if (!collected.equals(other.collected))
			return false;
		if (dorsalNumber != other.dorsalNumber)
			return false;
		if (inscriptionDate == null) {
			if (other.inscriptionDate != null)
				return false;
		} else if (!inscriptionDate.equals(other.inscriptionDate))
			return false;
		if (inscriptionId == null) {
			if (other.inscriptionId != null)
				return false;
		} else if (!inscriptionId.equals(other.inscriptionId))
			return false;
		if (raceId == null) {
			if (other.raceId != null)
				return false;
		} else if (!raceId.equals(other.raceId))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		return true;
	}
	

}