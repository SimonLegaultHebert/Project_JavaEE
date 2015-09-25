package service.forms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import eu.medsea.mimeutil.MimeUtil;
import service.beans.Client;

public class CreationClientForm {

	private static final String CHAMP_NOM = "nomClient";
	private static final String CHAMP_PRENOM = "prenomClient";
    private static final String CHAMP_ADRESSE = "adresseClient";
    private static final String CHAMP_TELEPHONE = "telephoneClient";
    private static final String CHAMP_EMAIL = "emailClient";
    private static final String CHAMP_IMAGE = "imageClient";

    private static final int TAILLE_TAMPON = 10240;
    
    private HashMap<String, String> erreurs = new HashMap<String, String>();
    private String resultat;
      
    public Client creerClient(HttpServletRequest req, String chemin){
    	String image = null;
    	Client client = new Client();
    	validationNom(client, req.getParameter(CHAMP_NOM).trim());
    	validationPrenom(client, req.getParameter(CHAMP_PRENOM).trim());
    	validationAdresse(client, req.getParameter(CHAMP_ADRESSE).trim());
    	validationTelephone(client, req.getParameter(CHAMP_TELEPHONE).trim());
    	validationEmail(client, req.getParameter(CHAMP_EMAIL).trim());
    	
    	try {
            image = validationImage(req, chemin);
        } catch ( FormValidationException e ) {
            rajouterErreur(CHAMP_IMAGE, e.getMessage());
        }
        client.setImage(image);
    	
    	
    	if (erreurs.isEmpty()) {
            resultat = "Succès de la création du client.";
        } else {
            resultat = "Échec de la création du client.";
        }
    	return client;
    }
    
    private void validationNom(Client client, String nom){
    	if(nom == null  || nom.length() < 2){
    		rajouterErreur(CHAMP_NOM, "Le nom doit contenir plus que deux caractères.");
    	}
    	client.setNom(nom);
    }
    
    private void validationPrenom(Client client, String prenom){
    	if(prenom == null  || prenom.length() < 2){
    		rajouterErreur(CHAMP_PRENOM, "Le prénom doit contenir plus que deux caractères.");
    	}
    	client.setPrenom(prenom);
    }
    
    private void validationAdresse(Client client, String adresse){
    	if(adresse == null || adresse.length() < 10){
    		rajouterErreur(CHAMP_ADRESSE, "L'adresse doit contenir plus que dix caractères.");
    	}
    	client.setAdresse(adresse);
    }
    
    private void validationTelephone(Client client, String numeroTelephone){
        if (numeroTelephone != null ) {
            if (!numeroTelephone.matches("^\\d+$")) {
            	rajouterErreur(CHAMP_TELEPHONE, "Le numéro de téléphone doit uniquement contenir des chiffres.");
            } else if (numeroTelephone.length() < 4) {
            	rajouterErreur(CHAMP_TELEPHONE, "Le numéro de téléphone doit contenir au moins 4 chiffres.");
            }
        } else {
            rajouterErreur(CHAMP_TELEPHONE, "Merci d'entrer un numéro de téléphone.");
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
    
    private String validationImage( HttpServletRequest request, String chemin ) throws FormValidationException {
        /*
         * Récupération du contenu du champ image du formulaire. Il faut ici
         * utiliser la méthode getPart().
         */
        String nomFichier = null;
        InputStream contenuFichier = null;
        try {
            Part part = request.getPart( CHAMP_IMAGE );
            nomFichier = getNomFichier( part );
            if ( nomFichier != null && !nomFichier.isEmpty() ) {
                /*
                 * Antibug pour Internet Explorer, qui transmet pour une raison
                 * mystique le chemin du fichier local à la machine du client...
                 * 
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * 
                 * On doit donc faire en sorte de ne sélectionner que le nom et
                 * l'extension du fichier, et de se débarrasser du superflu.
                 */
                nomFichier = nomFichier.substring( nomFichier.lastIndexOf( '/' ) + 1).substring(nomFichier.lastIndexOf('\\') + 1);

                /* Récupération du contenu du fichier */
                contenuFichier = part.getInputStream();

                /* Extraction du type MIME du fichier depuis l'InputStream */
                MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
                Collection<?> mimeTypes = MimeUtil.getMimeTypes( contenuFichier );

                /*
                 * Si le fichier est bien une image, alors son en-tête MIME
                 * commence par la chaîne "image"
                 */
                if (mimeTypes.toString().startsWith("image")) {
                    ecrireFichier( contenuFichier, nomFichier, chemin );
                } else {
                    throw new FormValidationException( "Le fichier envoyé doit être une image." );
                }
            }
        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            throw new FormValidationException( "Le fichier envoyé ne doit pas dépasser 1Mo." );
        } catch ( IOException e ) {
            /*
             * Exception retournée si une erreur au niveau des répertoires de
             * stockage survient (répertoire inexistant, droits d'accès
             * insuffisants, etc.)
             */
            e.printStackTrace();
            throw new FormValidationException( "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            /*
             * Exception retournée si la requête n'est pas de type
             * multipart/form-data.
             */
            e.printStackTrace();
            throw new FormValidationException(
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
        }

        return nomFichier;
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

	private static String getNomFichier(Part part) {
        /* Boucle sur chacun des paramètres de l'en-tête "content-disposition". */
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            /* Recherche de l'éventuelle présence du paramètre "filename". */
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                /*
                 * Si "filename" est présent, alors renvoi de sa valeur,
                 * c'est-à-dire du nom de fichier sans guillemets.
                 */
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        /* Et pour terminer, si rien n'a été trouvé... */
        return null;
    }
	
	private void ecrireFichier(InputStream contenuFichier, String nomFichier, String chemin) throws FormValidationException {
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux. */
            entree = new BufferedInputStream( contenuFichier, TAILLE_TAMPON );
            sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON );

            /*
             * Lit le fichier reçu et écrit son contenu dans un fichier sur le
             * disque.
             */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur = 0;
            while (( longueur = entree.read( tampon ) ) > 0){
                sortie.write( tampon, 0, longueur );
            }
        } catch ( Exception e ) {
            throw new FormValidationException( "Erreur lors de l'écriture du fichier sur le disque." );
        } finally {
            try {
                sortie.close();
            } catch (IOException ignore){
            }
            try {
                entree.close();
            } catch (IOException ignore){
            }
        }
    }

    
    
}

