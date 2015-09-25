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
    
    private String validationImage( HttpServletRequest request, String chemin ) throws FormValidationException {
        /*
         * R�cup�ration du contenu du champ image du formulaire. Il faut ici
         * utiliser la m�thode getPart().
         */
        String nomFichier = null;
        InputStream contenuFichier = null;
        try {
            Part part = request.getPart( CHAMP_IMAGE );
            nomFichier = getNomFichier( part );
            if ( nomFichier != null && !nomFichier.isEmpty() ) {
                /*
                 * Antibug pour Internet Explorer, qui transmet pour une raison
                 * mystique le chemin du fichier local � la machine du client...
                 * 
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * 
                 * On doit donc faire en sorte de ne s�lectionner que le nom et
                 * l'extension du fichier, et de se d�barrasser du superflu.
                 */
                nomFichier = nomFichier.substring( nomFichier.lastIndexOf( '/' ) + 1).substring(nomFichier.lastIndexOf('\\') + 1);

                /* R�cup�ration du contenu du fichier */
                contenuFichier = part.getInputStream();

                /* Extraction du type MIME du fichier depuis l'InputStream */
                MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
                Collection<?> mimeTypes = MimeUtil.getMimeTypes( contenuFichier );

                /*
                 * Si le fichier est bien une image, alors son en-t�te MIME
                 * commence par la cha�ne "image"
                 */
                if (mimeTypes.toString().startsWith("image")) {
                    ecrireFichier( contenuFichier, nomFichier, chemin );
                } else {
                    throw new FormValidationException( "Le fichier envoy� doit �tre une image." );
                }
            }
        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            throw new FormValidationException( "Le fichier envoy� ne doit pas d�passer 1Mo." );
        } catch ( IOException e ) {
            /*
             * Exception retourn�e si une erreur au niveau des r�pertoires de
             * stockage survient (r�pertoire inexistant, droits d'acc�s
             * insuffisants, etc.)
             */
            e.printStackTrace();
            throw new FormValidationException( "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            /*
             * Exception retourn�e si la requ�te n'est pas de type
             * multipart/form-data.
             */
            e.printStackTrace();
            throw new FormValidationException(
                    "Ce type de requ�te n'est pas support�, merci d'utiliser le formulaire pr�vu pour envoyer votre fichier." );
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
        /* Boucle sur chacun des param�tres de l'en-t�te "content-disposition". */
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            /* Recherche de l'�ventuelle pr�sence du param�tre "filename". */
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                /*
                 * Si "filename" est pr�sent, alors renvoi de sa valeur,
                 * c'est-�-dire du nom de fichier sans guillemets.
                 */
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        /* Et pour terminer, si rien n'a �t� trouv�... */
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
             * Lit le fichier re�u et �crit son contenu dans un fichier sur le
             * disque.
             */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur = 0;
            while (( longueur = entree.read( tampon ) ) > 0){
                sortie.write( tampon, 0, longueur );
            }
        } catch ( Exception e ) {
            throw new FormValidationException( "Erreur lors de l'�criture du fichier sur le disque." );
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

