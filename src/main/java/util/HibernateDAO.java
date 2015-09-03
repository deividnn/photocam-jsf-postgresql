/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author DeividNn
 * @param <T>
 */
public class HibernateDAO<T> implements DAO<T>, Serializable {

    private Session sessao;

    public HibernateDAO(Session sessao) {
        this.sessao = sessao;
    }
    
    
    @Override
    public boolean salvar(T t) {
        try {
            iniciarTransacao();
            sessao.saveOrUpdate(t);
            fazerCommit();
            return true;
        } catch (Exception e) {
            fazerRollback(e);
        }
        return false;
    }

    @Override
    public boolean excluir(T t) {
        try {
            iniciarTransacao();
            sessao.delete(t);
            fazerCommit();
            return true;
        } catch (Exception e) {
            fazerRollback(e);
        }
        return false;
    }

    
    @Override
    public List<T> listar(String hql) {
        try {
            iniciarTransacao();
            List<T> t = sessao.createQuery(hql).list();
            fazerCommit();
            return t;
        } catch (Exception e) {
            fazerRollback(e);
        }
        return null;
    }

    @Override
    public T pegar(String hql) {
        try {
            iniciarTransacao();
            T t = (T) sessao.createQuery(hql).setMaxResults(1).uniqueResult();
            fazerCommit();
            return t;
        } catch (Exception e) {
            fazerRollback(e);
        }
        return null;
    }

    private void fazerRollback(Exception e) {
        if (this.sessao.getTransaction() != null) {
            this.sessao.getTransaction().rollback();
            Util.criarMensagemErro("erro:" + e);
        }
    }

    private void fazerCommit() {
        this.sessao.getTransaction().commit();
    }

    private void iniciarTransacao() {
        this.sessao = Util.pegarSessao();
        if (this.sessao.getTransaction() != null
                && this.sessao.getTransaction().isActive()) {
            this.sessao.getTransaction();
        } else {
            this.sessao.beginTransaction();
        }
    }
}
