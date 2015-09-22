package service.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.beans.Commande;

public class SuppressionCommande extends HttpServlet{

	public static final String PARAM_DATE_COMMANDE = "dateCommande";
    public static final String SESSION_COMMANDES = "commandes";

    public static final String VUE = "/listeCommandes";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	String dateCommande = getValeurParametre(req, PARAM_DATE_COMMANDE);

        HttpSession session = req.getSession();
        Map<String, Commande> commandes = (HashMap<String, Commande>)session.getAttribute(SESSION_COMMANDES);
        if ( dateCommande != null && commandes != null ) {
            commandes.remove(dateCommande);
            session.setAttribute(SESSION_COMMANDES, commandes);
        }
        resp.sendRedirect(req.getContextPath() + VUE);
    }
    
    private static String getValeurParametre(HttpServletRequest req, String nomChamp) {
        String valeur = req.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }
}
