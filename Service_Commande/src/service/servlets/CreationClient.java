package service.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.beans.Client;

public class CreationClient extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String message = "Client crée avec succès";
		Client client = createClientWithRequestValues(req);				
		if(!client.isClientValid()){
			message = "Erreur - Vous n'avez pas rempli tous les champs obligatoires. <br> <a href=\"creerClient.jsp\">Cliquez ici</a> pour accéder au formulaire de création d'un client.";
		}
		req.setAttribute("message", message);
		req.setAttribute("client", client);
		this.getServletContext().getRequestDispatcher("/afficherClient.jsp").forward(req, resp);
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
