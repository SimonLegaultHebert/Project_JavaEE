package service.forms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import service.beans.Client;
import service.beans.Commande;

public class CreationCommandeForm {

	private static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
	private static final String CHAMP_DATE = "dateCommande";
	private static final String CHAMP_MONTANT = "montantCommande";
	private static final String CHAMP_MODE_PAIEMENT = "modePaiementCommande";
	private static final String CHAMP_STATUT_PAIEMENT = "statutPaiementCommande";
	private static final String CHAMP_MODE_LIVRAISON = "modeLivraisonCommande";
	private static final String CHAMP_STATUT_LIVRAISON = "statutLivraisonCommande";
	private static final Commande COMMANDE = new Commande();
	
	private String resultat;
	private HashMap<String, String> erreurs = new HashMap<String, String>();
	
	public Commande creerCommande(HttpServletRequest req){
		CreationClientForm clientForm = new CreationClientForm();
		Client client = clientForm.creerClient(req);
		erreurs = clientForm.getErreurs();
		COMMANDE.setClient(client);
		
		validationModePaiement(req.getParameter(CHAMP_MODE_PAIEMENT).trim());
		validationStatutPaiement(req.getParameter(CHAMP_STATUT_PAIEMENT).trim());
		validationModeLivraison(req.getParameter(CHAMP_MODE_LIVRAISON).trim());
		validationStatutLivraison(req.getParameter(CHAMP_STATUT_LIVRAISON).trim());
		validationMontant(req.getParameter(CHAMP_MONTANT).trim());
		creerDate();
		if (erreurs.isEmpty()) {
            resultat = "Succès de la création de la commande.";
        } else {
            resultat = "Échec de la création de la commande.";
        }
		return COMMANDE;
	}

	private void validationModePaiement(String modePaiement){
		if(modePaiement == null || modePaiement.length() < 2){
			rajouterErreur(CHAMP_MODE_PAIEMENT, "Le mode de paiement doit contenir au moins 2 caractères.");
		}
		COMMANDE.setModePaiement(modePaiement);
	}
	
	private void validationStatutPaiement(String statutPaiement){
		if(statutPaiement.length() < 2){
			rajouterErreur(CHAMP_STATUT_PAIEMENT, "Le statut de paiement doit contenir au moins 2 caractères.");
		}
		COMMANDE.setStatutPaiement(statutPaiement);
	}	
	
	private void validationModeLivraison(String modeLivraison){
		if(modeLivraison == null || modeLivraison.length() < 2){
			rajouterErreur(CHAMP_MODE_LIVRAISON, "Le mode de paiement doit contenir au moins 2 caractères.");		
		}
		COMMANDE.setModeLivraison(modeLivraison);
	}
	
	private void validationStatutLivraison(String statutLivraison){
		if(statutLivraison.length() < 2){
			rajouterErreur(CHAMP_STATUT_LIVRAISON, "Le statut de livraison doit contenir au moins 2 caractères.");
		}
		COMMANDE.setStatutLivraison(statutLivraison);
	}
	
	private void validationMontant(String montant){
		double dMontant = -1;
		if(montant == null){
			rajouterErreur(CHAMP_MONTANT, "Le montant ne doit pas être vide.");
		}else{
			try{
				dMontant = Double.parseDouble(montant);	
			}catch(NumberFormatException e){
				rajouterErreur(CHAMP_MONTANT, "Le montant doit contenir seulement des chiffres.");
			}
		}		
		COMMANDE.setMontant(dMontant);
	}
	
	private void creerDate(){
		DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
		Date date = new Date(); 
		COMMANDE.setDate(dateFormat.format(date));
	}
	
	private void rajouterErreur(String champ, String message){
		erreurs.put(champ, message);
	}
	
	public String getResultat() {
		return resultat;
	}

	public HashMap<String, String> getErreurs() {
		return erreurs;
	}

	

	
	
}
