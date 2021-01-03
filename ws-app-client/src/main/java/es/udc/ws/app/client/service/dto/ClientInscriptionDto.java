package es.udc.ws.app.client.service.dto;

public class ClientInscriptionDto {

    private Long inscriptionId;
    private Long raceId;
    private int dorsalNumber;
    private Boolean collected;
    private String creditCardNumber;

    public ClientInscriptionDto(Long inscriptionId, Long raceId, int dorsalNumber, Boolean collected) {
        this.inscriptionId = inscriptionId;
        this.raceId = raceId;
        this.dorsalNumber = dorsalNumber;
        this.collected = collected;
    }

    public ClientInscriptionDto(Long inscriptionId, String creditCardNumber) {
        this.inscriptionId = inscriptionId;
        this.creditCardNumber = creditCardNumber;
    }

    public Long getInscriptionId() { return inscriptionId; }
    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public Long getRaceId() {
        return raceId;
    }
    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public int getDorsalNumber() { return dorsalNumber; }
    public void setDorsalNumber(int dorsalNumber) {
        this.dorsalNumber = dorsalNumber;
    }

    public Boolean getCollected() {
        return collected;
    }
    public void setCollected(Boolean collected) {
        this.collected = collected;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((collected == null) ? 0 : collected.hashCode());
        result = prime * result + dorsalNumber;
        result = prime * result + ((inscriptionId == null) ? 0 : inscriptionId.hashCode());
        result = prime * result + ((raceId == null) ? 0 : raceId.hashCode());
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
        ClientInscriptionDto other = (ClientInscriptionDto) obj;
        if (collected == null) {
            if (other.collected != null)
                return false;
        } else if (!collected.equals(other.collected))
            return false;
        if (dorsalNumber != other.dorsalNumber)
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
        return true;
    }
}
