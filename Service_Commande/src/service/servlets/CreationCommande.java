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
import service.forms.CreationCommandeForm;

public class CreationCommande extends HttpServlet {

 
    public static final String ATT_FORM = "form";
    public static final String ATT_COMMANDE = "commande";

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
			this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(req, resp);
		}else{
			this.getServletContext().getRequestDispatcher(VUE_FORM).forward(req, resp);
		}
    }
	

	

	
}
