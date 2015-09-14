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
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String message = "Commande créée avec succès !";
		Commande commande = createCommandeWithRequestValues(req);
		Client client = createClientWithRequestValues(req);
		
		if(!client.isClientValid()|| !commande.isCommandeValid()){
			message = "Erreur - Vous n'avez pas rempli tous les champs obligatoires. <br> <a href=\"creerCommande.jsp\">Cliquez ici</a> pour accéder au formulaire de création d'une commande.";
		}
		req.setAttribute("commande", commande);
		req.setAttribute("message", message);
		this.getServletContext().getRequestDispatcher("/afficherCommande.jsp").forward(req, resp);
	}
	
	private Commande createCommandeWithRequestValues(HttpServletRequest req){
		Commande commande = new Commande();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(); 
		commande.setDate(dateFormat.format(date));
		double montant;
		try{
			montant = Double.parseDouble(req.getParameter("montantCommande").trim());	
		}catch(NumberFormatException e){
			montant = -1;
		}
		commande.setMontant(montant);
		commande.setModeLivraison(req.getParameter("modeLivraisonCommande").trim());
		commande.setModePaiement(req.getParameter("modePaiementCommande").trim());
		commande.setModeLivraison(req.getParameter("modeLivraisonCommande").trim());
		commande.setStatutLivraison(req.getParameter("statutLivraisonCommande").trim());		
		return commande;	
	}
	
	private Client createClientWithRequestValues(HttpServletRequest req){
		Client client = new Client();
		client.setNom(req.getParameter("nomClient").trim());
		client.setPrenom(req.getParameter("prenomClient").trim());
		client.setAdresse(req.getParameter("adresseClient").trim());
		client.setNumeroTelephone(req.getParameter("telephoneClient").trim());
		client.setEmail(req.getParameter("emailClient").trim());		
		return client;
	}

	
}
