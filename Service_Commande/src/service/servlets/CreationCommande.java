package service.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.beans.Client;
import service.beans.Commande;
import service.forms.CreationCommandeForm;

public class CreationCommande extends HttpServlet {

 
    public static final String ATT_FORM = "form";
    public static final String ATT_COMMANDE = "commande";
    public static final String SESSION_CLIENTS = "clients";
    public static final String SESSION_COMMANDES = "commandes";

    public static final String VUE_FORM = "/WEB-INF/creerCommande.jsp";
    public static final String VUE_SUCCES = "/WEB-INF/afficherCommande.jsp";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	this.getServletContext().getRequestDispatcher(VUE_FORM).forward(req, resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	CreationCommandeForm form = new CreationCommandeForm();
    	Commande commande = form.creerCommande(req);
    	req.setAttribute(ATT_COMMANDE, commande);
    	req.setAttribute(ATT_FORM, form);
    	
    	if(form.getErreurs().isEmpty()){
    		HttpSession session = req.getSession();
            Map<String, Client> clients = (HashMap<String, Client>) session.getAttribute( SESSION_CLIENTS );
            if ( clients == null ) {
                clients = new HashMap<String, Client>();
            }
            clients.put( commande.getClient().getNom(), commande.getClient() );
            session.setAttribute( SESSION_CLIENTS, clients );

            Map<String, Commande> commandes = (HashMap<String, Commande>) session.getAttribute( SESSION_COMMANDES );
            if ( commandes == null ) {
                commandes = new HashMap<String, Commande>();
            }
            commandes.put( commande.getDate(), commande );
            session.setAttribute( SESSION_COMMANDES, commandes );
			this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(req, resp);
		}else{
			this.getServletContext().getRequestDispatcher(VUE_FORM).forward(req, resp);
		}
    }
	

	

	
}
