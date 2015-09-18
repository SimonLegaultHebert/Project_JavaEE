package service.forms;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import service.beans.Client;

public class CreationClientForm {

	private static final String CHAMP_NOM = "nomClient";
	private static final String CHAMP_PRENOM = "prenomClient";
    private static final String CHAMP_ADRESSE = "adresseClient";
    private static final String CHAMP_TELEPHONE = "telephoneClient";
    private static final String CHAMP_EMAIL = "emailClient";
    private static final Client CLIENT = new Client();
    
    private HashMap<String, String> erreurs = new HashMap<String, String>();
    private String resultat;
      
    public Client creerClient(HttpServletRequest req){
    	validationNom(req.getParameter(CHAMP_NOM).trim());
    	validationPrenom(req.getParameter(CHAMP_PRENOM).trim());
    	validationAdresse(req.getParameter(CHAMP_ADRESSE).trim());
    	validationTelephone(req.getParameter(CHAMP_TELEPHONE).trim());
    	validationEmail(req.getParameter(CHAMP_EMAIL).trim());
    	if (erreurs.isEmpty()) {
            resultat = "Succès de la création du client.";
        } else {
            resultat = "Échec de la création du client.";
        }
    	return CLIENT;
    }
    
    private void validationNom(String nom){
    	if(nom == null  || nom.length() < 2){
    		rajouterErreur(CHAMP_NOM, "Le nom doit contenir plus que deux caractères.");
    	}
    	CLIENT.setNom(nom);
    }
    
    private void validationPrenom(String prenom){
    	if(prenom == null  || prenom.length() < 2){
    		rajouterErreur(CHAMP_PRENOM, "Le prénom doit contenir plus que deux caractères.");
    	}
    	CLIENT.setPrenom(prenom);
    }
    
    private void validationAdresse(String adresse){
    	if(adresse == null || adresse.length() < 10){
    		rajouterErreur(CHAMP_ADRESSE, "L'adresse doit contenir plus que dix caractères.");
    	}
    	CLIENT.setAdresse(adresse);
    }
    
    private void validationTelephone(String numeroTelephone){
        if (numeroTelephone != null ) {
            if (!numeroTelephone.matches("^\\d+$")) {
            	rajouterErreur(CHAMP_TELEPHONE, "Le numéro de téléphone doit uniquement contenir des chiffres.");
            } else if (numeroTelephone.length() < 4) {
            	rajouterErreur(CHAMP_TELEPHONE, "Le numéro de téléphone doit contenir au moins 4 chiffres.");
            }
        } else {
            rajouterErreur(CHAMP_TELEPHONE, "Merci d'entrer un numéro de téléphone.");
        }
        CLIENT.setNumeroTelephone(numeroTelephone);
    }

    
    private void validationEmail(String email){
		if(email != null && !email.isEmpty()){
			if(!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")){
				rajouterErreur(CHAMP_EMAIL, "Merci de saisir une adresse mail valide.");
			}		
		}else{
			rajouterErreur(CHAMP_EMAIL, "Merci de saisir une adresse mail valide.");
		}
		CLIENT.setEmail(email);
	}
    
    private void rajouterErreur(String champ, String message){
    	erreurs.put(champ, message);
    }

	public HashMap<String, String> getErreurs() {
		return erreurs;
	}

	public String getResultat() {
		return resultat;
	}


    
    
}

