package service.beans;

public class Commande {

	private Client client;
	private String date;
	private double montant;
	private String modePaiement;
	private String statutPaiement;
	private String modeLivraison;
	private String statutLivraison;
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public String getModePaiement() {
		return modePaiement;
	}
	public void setModePaiement(String modePaiement) {
		this.modePaiement = modePaiement;
	}
	public String getStatutPaiement() {
		return statutPaiement;
	}
	public void setStatutPaiement(String statutPaiement) {
		this.statutPaiement = statutPaiement;
	}
	public String getModeLivraison() {
		return modeLivraison;
	}
	public void setModeLivraison(String modeLivraison) {
		this.modeLivraison = modeLivraison;
	}
	public String getStatutLivraison() {
		return statutLivraison;
	}
	public void setStatutLivraison(String statutLivraison) {
		this.statutLivraison = statutLivraison;
	}
	
	public boolean isCommandeValid(){
		boolean isValid = true;
		if(this.modePaiement.isEmpty() || this.modeLivraison.isEmpty() || this.montant == -1){
			isValid = false;
		}
		return isValid;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((modeLivraison == null) ? 0 : modeLivraison.hashCode());
		result = prime * result + ((modePaiement == null) ? 0 : modePaiement.hashCode());
		long temp;
		temp = Double.doubleToLongBits(montant);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((statutLivraison == null) ? 0 : statutLivraison.hashCode());
		result = prime * result + ((statutPaiement == null) ? 0 : statutPaiement.hashCode());
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
		Commande other = (Commande) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (modeLivraison == null) {
			if (other.modeLivraison != null)
				return false;
		} else if (!modeLivraison.equals(other.modeLivraison))
			return false;
		if (modePaiement == null) {
			if (other.modePaiement != null)
				return false;
		} else if (!modePaiement.equals(other.modePaiement))
			return false;
		if (Double.doubleToLongBits(montant) != Double.doubleToLongBits(other.montant))
			return false;
		if (statutLivraison == null) {
			if (other.statutLivraison != null)
				return false;
		} else if (!statutLivraison.equals(other.statutLivraison))
			return false;
		if (statutPaiement == null) {
			if (other.statutPaiement != null)
				return false;
		} else if (!statutPaiement.equals(other.statutPaiement))
			return false;
		return true;
	}
	

}
