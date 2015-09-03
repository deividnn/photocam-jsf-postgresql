/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;

/**
 *
 * @author DeividNn
 */
@ManagedBean
@ViewScoped
public class Util implements Serializable {

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

    private String caminho;
    private String arquivo;

    public void gerarRelatorio(String jasper, Long id) {
        System.out.println(id);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        ServletContext servletContext = (ServletContext) context.getContext();
        String arquivoJasper = "/WEB-INF/relatorios/" + jasper + ".jasper";
        try {

            Map<String, Object> param = new HashMap<>();
            param.put("id", id);
            this.arquivo = gerarMostrar(request, arquivoJasper, servletContext,
                    pegarConexao(), jasper, param);
            Util.atualizarComponente("relatorio");
            Util.executarJavascript("PF('dlgrelatorio').show();");

        } catch (IOException ex) {
            Util.criarMensagemErro(ex.getMessage());
        }

    }

    public String gerarMostrar(HttpServletRequest request,
            String arquivoJasper,
            ServletContext contexto,
            Connection conexao,
            String nomeArquivo,
            Map<String, Object> param) throws IOException {

        FileInputStream reportFile = new FileInputStream(
                contexto.getRealPath(arquivoJasper));

        byte[] bytes;

        try {
            ServletContext servletContext = (ServletContext) FacesContext.
                    getCurrentInstance().getExternalContext().getContext();
            String absoluteDiskPath = servletContext.getRealPath("/resources/fotossalvas");

            Locale l = new Locale("pt", "BR");
            param.put(JRParameter.REPORT_LOCALE, l);

            bytes = JasperRunManager.runReportToPdf(reportFile, param, conexao);
            String nome = String.valueOf(new Date().getTime());
            System.out.println(nome);
            File a = new File(absoluteDiskPath + File.separator + nome + ".pdf");
            this.caminho = a.getAbsolutePath();
            FileUtils.writeByteArrayToFile(a, bytes);

            return nome;

        } catch (JRException e) {
            System.out.println(e);
            Util.criarMensagemErro(e.getMessage());
        } finally {
            try {
                pegarConexao().close();
            } catch (SQLException ex) {
                Util.criarMensagemErro(ex.getMessage());
            }
        }
        return null;

    }

    public static Connection pegarConexao() {
        Connection conexao = null;
        try {
            Class.forName("org.postgresql.Driver");

            conexao = DriverManager.getConnection("jdbc:postgresql://"
                    + "localhost:5432"
                    + "/photocam?charSet=UTF-8",
                    "deivid",
                    "deivid");
        } catch (SQLException ex) {
            Util.criarMensagemErro(ex.getMessage());
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return conexao;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

}
