/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author DeividNn
 */
@WebListener
public class Contexto implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    HibernateUtil.getSessionFactory().close();
    }
    
}
