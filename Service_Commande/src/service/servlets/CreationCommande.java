package service.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.beans.Client;
import service.beans.Commande;

public class CreationCommande extends HttpServlet {
	
	public static final String CHAMP_NOM              = "nomClient";
    public static final String CHAMP_PRENOM           = "prenomClient";
    public static final String CHAMP_ADRESSE          = "adresseClient";
    public static final String CHAMP_TELEPHONE        = "telephoneClient";
    public static final String CHAMP_EMAIL            = "emailClient";
 
    public static final String CHAMP_DATE             = "dateCommande";
    public static final String CHAMP_MONTANT          = "montantCommande";
    public static final String CHAMP_MODE_PAIEMENT    = "modePaiementCommande";
    public static final String CHAMP_STATUT_PAIEMENT  = "statutPaiementCommande";
    public static final String CHAMP_MODE_LIVRAISON   = "modeLivraisonCommande";
    public static final String CHAMP_STATUT_LIVRAISON = "statutLivraisonCommande";
 
    public static final String ATT_COMMANDE           = "commande";
    public static final String ATT_MESSAGE            = "message";
    public static final String ATT_ERREUR             = "erreur";
 
    public static final String FORMAT_DATE            = "dd/MM/yyyy HH:mm:ss";
 
    public static final String VUE                    = "/afficherCommande.jsp";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String message = "Commande créée avec succès !";
		boolean erreur = false;
		Commande commande = createCommandeWithRequestValues(req);
		Client client = createClientWithRequestValues(req);
		
		if(!client.isClientValid()|| !commande.isCommandeValid()){
			erreur = true;
			message = "Erreur - Vous n'avez pas rempli tous les champs obligatoires. <br> <a href=\"creerCommande.jsp\">Cliquez ici</a> pour accéder au formulaire de création d'une commande.";
		}
		req.setAttribute(ATT_ERREUR, erreur);
		req.setAttribute(ATT_COMMANDE, commande);
		req.setAttribute(ATT_MESSAGE, message);
		this.getServletContext().getRequestDispatcher(VUE).forward(req, resp);
	}
	
	private Commande createCommandeWithRequestValues(HttpServletRequest req){
		Commande commande = new Commande();
		DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
		Date date = new Date(); 
		commande.setDate(dateFormat.format(date));
		double montant;
		try{
			montant = Double.parseDouble(req.getParameter(CHAMP_MONTANT).trim());	
		}catch(NumberFormatException e){
			montant = -1;
		}
		commande.setMontant(montant);
		commande.setModeLivraison(req.getParameter(CHAMP_MODE_LIVRAISON).trim());
		commande.setModePaiement(req.getParameter(CHAMP_MODE_PAIEMENT).trim());
		commande.setModeLivraison(req.getParameter(CHAMP_MODE_LIVRAISON).trim());
		commande.setStatutLivraison(req.getParameter(CHAMP_STATUT_LIVRAISON).trim());		
		return commande;	
	}
	
	private Client createClientWithRequestValues(HttpServletRequest req){
		Client client = new Client();
		client.setNom(req.getParameter(CHAMP_NOM).trim());
		client.setPrenom(req.getParameter(CHAMP_PRENOM ).trim());
		client.setAdresse(req.getParameter(CHAMP_ADRESSE).trim());
		client.setNumeroTelephone(req.getParameter(CHAMP_TELEPHONE).trim());
		client.setEmail(req.getParameter(CHAMP_EMAIL).trim());		
		return client;
	}

	
}
