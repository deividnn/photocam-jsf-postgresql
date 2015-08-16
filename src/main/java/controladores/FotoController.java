package controladores;

import entidades.Foto;
import java.util.List;
import util.HibernateDAO;
import util.Util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Magazine L
 */
public class FotoController {

    private final HibernateDAO dao;

    public FotoController() {
        this.dao = new HibernateDAO(Util.pegarSessao());
    }

    public boolean salvar(Foto foto) {
        try {
            this.dao.salvar(foto);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
    
    public boolean excluir(Foto foto) {
        try {
            this.dao.excluir(foto);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public List<Foto> listar(String hql) {
        return this.dao.listar(hql);

    }

    public Foto pegar(String hql) {
        return (Foto) this.dao.pegar(hql);

    }

}
