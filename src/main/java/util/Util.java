/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Magazine L
 */
public class Util {

    public static Session pegarSessao() {
        try {
            return HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (Exception e) {
            System.out.println("erro(util): " + e);
        }
        return null;
    }

    public static void executarJavascript(String comando) {
        RequestContext.getCurrentInstance().execute(comando);
    }

    public static void resetarComponente(String id) {
        RequestContext.getCurrentInstance().reset(id);
    }

    public static void atualizarComponente(String id) {
        RequestContext.getCurrentInstance().update(id);
    }

    public static void criarMensagemErro(String texto) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, texto);
        FacesContext.getCurrentInstance().addMessage(texto, msg);
    }

    public static void criarMensagemAviso(String texto) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, texto, texto);
        FacesContext.getCurrentInstance().addMessage(texto, msg);
    }

    public static void criarMensagem(String texto) {
        FacesMessage msg = new FacesMessage(texto);
        FacesContext.getCurrentInstance().addMessage(texto, msg);
    }
}
