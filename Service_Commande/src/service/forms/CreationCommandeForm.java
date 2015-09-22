package service.forms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import service.beans.Client;
import service.beans.Commande;

public class CreationCommandeForm {

	private static final String CHAMP_CHOIX_CLIENT = "choixNouveauClient";
    private static final String CHAMP_LISTE_CLIENTS = "listeClients";
	private static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
	private static final String CHAMP_DATE = "dateCommande";
	private static final String CHAMP_MONTANT = "montantCommande";
	private static final String CHAMP_MODE_PAIEMENT = "modePaiementCommande";
	private static final String CHAMP_STATUT_PAIEMENT = "statutPaiementCommande";
	private static final String CHAMP_MODE_LIVRAISON = "modeLivraisonCommande";
	private static final String CHAMP_STATUT_LIVRAISON = "statutLivraisonCommande";
	
	private static final String ANCIEN_CLIENT = "ancienClient";
    private static final String SESSION_CLIENTS = "clients";
	
	private String resultat;
	private HashMap<String, String> erreurs = new HashMap<String, String>();
	
	public Commande creerCommande(HttpServletRequest req){
		Client client;
		Commande commande = new Commande();
		String choixNouveauClient = getValeurChamp(req, CHAMP_CHOIX_CLIENT);
		if(ANCIEN_CLIENT.equals(choixNouveauClient)){
			String nomAncienClient = getValeurChamp(req, CHAMP_LISTE_CLIENTS);
			HttpSession session = req.getSession();
			client = ((HashMap<String, Client>) session.getAttribute(SESSION_CLIENTS)).get(nomAncienClient);
		}else{
			CreationClientForm clientForm = new CreationClientForm();
			client = clientForm.creerClient(req);
			erreurs = clientForm.getErreurs();
		}
		commande.setClient(client);
				
		validationModePaiement(commande, req.getParameter(CHAMP_MODE_PAIEMENT).trim());
		validationStatutPaiement(commande, req.getParameter(CHAMP_STATUT_PAIEMENT).trim());
		validationModeLivraison(commande, req.getParameter(CHAMP_MODE_LIVRAISON).trim());
		validationStatutLivraison(commande, req.getParameter(CHAMP_STATUT_LIVRAISON).trim());
		validationMontant(commande, req.getParameter(CHAMP_MONTANT).trim());
		creerDate(commande);
		if (erreurs.isEmpty()) {
            resultat = "Succès de la création de la commande.";
        } else {
            resultat = "Échec de la création de la commande.";
        }
		return commande;
	}

	private void validationModePaiement(Commande commande, String modePaiement){
		if(modePaiement == null || modePaiement.length() < 2){
			rajouterErreur(CHAMP_MODE_PAIEMENT, "Le mode de paiement doit contenir au moins 2 caractères.");
		}
		commande.setModePaiement(modePaiement);
	}
	
	private void validationStatutPaiement(Commande commande, String statutPaiement){
		if(statutPaiement.length() < 2){
			rajouterErreur(CHAMP_STATUT_PAIEMENT, "Le statut de paiement doit contenir au moins 2 caractères.");
		}
		commande.setStatutPaiement(statutPaiement);
	}	
	
	private void validationModeLivraison(Commande commande, String modeLivraison){
		if(modeLivraison == null || modeLivraison.length() < 2){
			rajouterErreur(CHAMP_MODE_LIVRAISON, "Le mode de paiement doit contenir au moins 2 caractères.");		
		}
		commande.setModeLivraison(modeLivraison);
	}
	
	private void validationStatutLivraison(Commande commande, String statutLivraison){
		if(statutLivraison.length() < 2){
			rajouterErreur(CHAMP_STATUT_LIVRAISON, "Le statut de livraison doit contenir au moins 2 caractères.");
		}
		commande.setStatutLivraison(statutLivraison);
	}
	
	private void validationMontant(Commande commande, String montant){
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
		commande.setMontant(dMontant);
	}
	
	private void creerDate(Commande commande){
		DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
		Date date = new Date(); 
		commande.setDate(dateFormat.format(date));
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

	private static String getValeurChamp(HttpServletRequest req, String nomChamp) {
        String valeur = req.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }

	
	
}
