package service.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.beans.Client;

public class CreationClient extends HttpServlet{
    public static final String CHAMP_NOM = "nomClient";
    public static final String CHAMP_PRENOM = "prenomClient";
    public static final String CHAMP_ADRESSE = "adresseClient";
    public static final String CHAMP_TELEPHONE = "telephoneClient";
    public static final String CHAMP_EMAIL = "emailClient";
    
    public static final String ATT_CLIENT = "client";
    public static final String ATT_MESSAGE = "message";
    public static final String ATT_ERREUR = "erreur";
    
    public static final String VUE = "/afficherClient.jsp";
    
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String message = "Client crée avec succès";
		boolean erreur = false;
		Client client = createClientWithRequestValues(req);				
		if(!client.isClientValid()){
			erreur = true;
			message = "Erreur - Vous n'avez pas rempli tous les champs obligatoires. <br> <a href=\"creerClient.jsp\">Cliquez ici</a> pour accéder au formulaire de création d'un client.";
		}
		req.setAttribute(ATT_ERREUR, erreur);
		req.setAttribute(ATT_MESSAGE, message);
		req.setAttribute(ATT_CLIENT, client);
		this.getServletContext().getRequestDispatcher(VUE).forward(req, resp);
	}
	
	private Client createClientWithRequestValues(HttpServletRequest req){
		Client client = new Client();
		client.setNom(req.getParameter(CHAMP_NOM).trim());
		client.setPrenom(req.getParameter(CHAMP_PRENOM).trim());
		client.setAdresse(req.getParameter(CHAMP_ADRESSE).trim());
		client.setNumeroTelephone(req.getParameter(CHAMP_ADRESSE).trim());
		client.setEmail(req.getParameter(CHAMP_EMAIL).trim());		
		return client;
	}

}
