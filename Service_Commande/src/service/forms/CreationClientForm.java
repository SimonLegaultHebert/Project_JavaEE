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
    
    private HashMap<String, String> erreurs = new HashMap<String, String>();
    private String resultat;
      
    public Client creerClient(HttpServletRequest req){
    	Client client = new Client();
    	validationNom(client, req.getParameter(CHAMP_NOM).trim());
    	validationPrenom(client, req.getParameter(CHAMP_PRENOM).trim());
    	validationAdresse(client, req.getParameter(CHAMP_ADRESSE).trim());
    	validationTelephone(client, req.getParameter(CHAMP_TELEPHONE).trim());
    	validationEmail(client, req.getParameter(CHAMP_EMAIL).trim());
    	if (erreurs.isEmpty()) {
            resultat = "Succ�s de la cr�ation du client.";
        } else {
            resultat = "�chec de la cr�ation du client.";
        }
    	return client;
    }
    
    private void validationNom(Client client, String nom){
    	if(nom == null  || nom.length() < 2){
    		rajouterErreur(CHAMP_NOM, "Le nom doit contenir plus que deux caract�res.");
    	}
    	client.setNom(nom);
    }
    
    private void validationPrenom(Client client, String prenom){
    	if(prenom == null  || prenom.length() < 2){
    		rajouterErreur(CHAMP_PRENOM, "Le pr�nom doit contenir plus que deux caract�res.");
    	}
    	client.setPrenom(prenom);
    }
    
    private void validationAdresse(Client client, String adresse){
    	if(adresse == null || adresse.length() < 10){
    		rajouterErreur(CHAMP_ADRESSE, "L'adresse doit contenir plus que dix caract�res.");
    	}
    	client.setAdresse(adresse);
    }
    
    private void validationTelephone(Client client, String numeroTelephone){
        if (numeroTelephone != null ) {
            if (!numeroTelephone.matches("^\\d+$")) {
            	rajouterErreur(CHAMP_TELEPHONE, "Le num�ro de t�l�phone doit uniquement contenir des chiffres.");
            } else if (numeroTelephone.length() < 4) {
            	rajouterErreur(CHAMP_TELEPHONE, "Le num�ro de t�l�phone doit contenir au moins 4 chiffres.");
            }
        } else {
            rajouterErreur(CHAMP_TELEPHONE, "Merci d'entrer un num�ro de t�l�phone.");
        }
        client.setNumeroTelephone(numeroTelephone);
    }

    
    private void validationEmail(Client client, String email){
		if(email != null && !email.isEmpty()){
			if(!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")){
				rajouterErreur(CHAMP_EMAIL, "Merci de saisir une adresse mail valide.");
			}		
		}else{
			rajouterErreur(CHAMP_EMAIL, "Merci de saisir une adresse mail valide.");
		}
		client.setEmail(email);
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

