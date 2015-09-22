package service.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.beans.Client;

public class SuppressionClient extends HttpServlet{

	public static final String PARAM_NOM_CLIENT = "nomClient";
	public static final String SESSION_CLIENTS = "clients";
	public static final String VUE = "/listeClients";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String nomClient = getValeurParametre(req, PARAM_NOM_CLIENT);
		
		HttpSession session = req.getSession();
		Map<String, Client> clients = (HashMap<String, Client>)session.getAttribute(SESSION_CLIENTS);
		if (nomClient != null && clients != null) {
            clients.remove( nomClient );
            session.setAttribute( SESSION_CLIENTS, clients );
        }
		
		resp.sendRedirect(req.getContextPath() + VUE);
	}
	
	private static String getValeurParametre(HttpServletRequest req, String nomChamp) {
        String valeur = req.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0){
            return null;
        } else {
            return valeur;
        }
    }
}
