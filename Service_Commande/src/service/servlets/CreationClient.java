package service.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.beans.Client;
import service.forms.CreationClientForm;

public class CreationClient extends HttpServlet{    
    public static final String ATT_CLIENT = "client";
    public static final String ATT_FORM = "form";
    public static final String SESSION_CLIENTS = "clients";
    
    public static final String VUE_SUCCES = "/WEB-INF/afficherClient.jsp";
    public static final String VUE_FORM = "/WEB-INF/creerClient.jsp";
    
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		this.getServletContext().getRequestDispatcher(VUE_FORM).forward(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		CreationClientForm form = new CreationClientForm();
		Client client = form.creerClient(req);		
		req.setAttribute(ATT_FORM, form);
		req.setAttribute(ATT_CLIENT, client);
		
		if(form.getErreurs().isEmpty()){
			HttpSession session = req.getSession();
	        Map<String, Client> clients = (HashMap<String, Client>)session.getAttribute(SESSION_CLIENTS);
	        if(clients == null){
	        	clients = new HashMap<String, Client>();
	        }
	        clients.put(client.getNom(), client);
	        session.setAttribute(SESSION_CLIENTS, clients);
			this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(req, resp);
		}else{
			this.getServletContext().getRequestDispatcher(VUE_FORM).forward(req, resp);
		}
					
	}
	
}
