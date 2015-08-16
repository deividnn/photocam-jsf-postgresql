/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controladores.FotoController;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ImageStream implements Serializable {

 

    public StreamedContent getFoto() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            String ids = context.getExternalContext().getRequestParameterMap().get("id");
            Long id = Long.valueOf(ids);
            System.out.println("foto" + id);
            String hql = "select vo from Foto vo where vo.id=" + id + "";
            System.out.println(hql);
            StreamedContent i = new DefaultStreamedContent(
                    new ByteArrayInputStream(
                            new FotoController().pegar(hql).getArquivo()));
            return i;
        }
    }

   
}
