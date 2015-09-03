package beans;

import controladores.FotoController;
import entidades.Foto;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import util.Util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DeividNn
 */
@ManagedBean
@ViewScoped
public class FotoBean implements Serializable {

    private Foto foto;
    private List<Foto> fotos;
    private List<Foto> arquivos;
    private boolean abriuWebcam;
    private String arquivo;
    private File fotosalva;
    StreamedContent fotoc;

    @PostConstruct
    public void init() {
        listarFotos();
        listarArquivos();
    }

    public void listarFotos() {
        fotos = new ArrayList<>();
        String hql = "select vo from Foto vo where vo.nome is null order by vo.dataHora desc";
        fotos = new FotoController().listar(hql);
    }

    public void listarArquivos() {
        arquivos = new ArrayList<>();
        String hql = "select vo from Foto vo where vo.nome is not null order by vo.dataHora desc";
        arquivos = new FotoController().listar(hql);
    }

    public void abrirJanelaFoto() {
        abriuWebcam = true;
        arquivo = "";
        foto = new Foto();
        Util.atualizarComponente("dlgfoto");
        Util.executarJavascript("PF('dlgfoto').show();");
    }

    public void excluir(Foto f) {
        try {
            if (new FotoController().excluir(f)) {
                listarFotos();
                Util.criarMensagemAviso("foto excluida");
                Util.atualizarComponente("foto");

                abriuWebcam = false;
            }
        } catch (Exception e) {
            Util.criarMensagemErro(e.toString());
        }
    }

    public String gerarNome() {
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public void tirarFoto(CaptureEvent captureEvent) throws IOException {
        arquivo = gerarNome();
        byte[] data = captureEvent.getData();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                .getContext();
        String pasta = servletContext.getRealPath("")
                + File.separator + "resources"
                + File.separator + "fotossalvas";
        File vpasta = new File(pasta);
        if (!vpasta.exists()) {
            vpasta.setWritable(true);
            vpasta.mkdirs();
        }
        String novoarquivo = pasta + File.separator + arquivo + ".jpeg";
        System.out.println(novoarquivo);
        fotosalva = new File(novoarquivo);
        fotosalva.createNewFile();
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(fotosalva);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();

        } catch (IOException e) {
            Util.criarMensagemErro(e.toString());

        }
    }

    public void salvarFoto() {
        try {
            byte[] fotobyte = FileUtils.readFileToByteArray(fotosalva);
            foto.setArquivo(fotobyte);
            foto.setDataHora(Calendar.getInstance().getTime());

            if (new FotoController().salvar(foto)) {
                listarFotos();
                Util.executarJavascript("PF('dlgfoto').hide()");
                Util.criarMensagem("foto salva");
                Util.atualizarComponente("foto");
                fotosalva.delete();
                abriuWebcam = false;
            }
        } catch (Exception e) {
            Util.criarMensagemErro(e.toString());
        }
    }

    public void abrirJanelaUpload() {
        foto = new Foto();
        Util.atualizarComponente("dlgfoto");
        Util.executarJavascript("PF('dlgfoto').show();");
    }

    public void enviarArquivo(FileUploadEvent event) {
        try {
            arquivo = gerarNome();
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                    .getContext();
            String pasta = servletContext.getRealPath("")
                    + File.separator + "resources"
                    + File.separator + "arquivossalvos";
            File vpasta = new File(pasta);
            if (!vpasta.exists()) {
                vpasta.setWritable(true);
                vpasta.mkdirs();
            }
            String novoarquivo = pasta + File.separator + event.getFile().getFileName();
            File a = new File(novoarquivo);
            a.createNewFile();
            FileUtils.copyInputStreamToFile(event.getFile().getInputstream(), a);
            byte[] fotobyte = FileUtils.readFileToByteArray(a);
            foto.setNome(event.getFile().getFileName());
            foto.setArquivo(fotobyte);
            foto.setDataHora(Calendar.getInstance().getTime());

            if (new FotoController().salvar(foto)) {
                listarArquivos();
                Util.executarJavascript("PF('dlgfoto').hide()");
                Util.criarMensagem("arquivo salvo");
                Util.atualizarComponente("foto");
                a.delete();
            }

        } catch (Exception e) {
            Util.criarMensagemErro(e.toString());
        }
    }

    public StreamedContent fazerDownload(Foto f) {
        return new DefaultStreamedContent(
                new ByteArrayInputStream(f.getArquivo()), "application/octet-stream", f.getNome());
    }

    public void excluirArquivo(Foto f) {
        try {
            if (new FotoController().excluir(f)) {
                listarArquivos();
                Util.criarMensagemAviso("arquivo excluido");
                Util.atualizarComponente("foto");

            }
        } catch (Exception e) {
            Util.criarMensagemErro(e.toString());
        }
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public boolean isAbriuWebcam() {
        return abriuWebcam;
    }

    public void setAbriuWebcam(boolean abriuWebcam) {
        this.abriuWebcam = abriuWebcam;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public File getFotosalva() {
        return fotosalva;
    }

    public void setFotosalva(File fotosalva) {
        this.fotosalva = fotosalva;
    }

    public StreamedContent getFotoc() {
        return fotoc;
    }

    public void setFotoc(StreamedContent fotoc) {
        this.fotoc = fotoc;
    }

    public List<Foto> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Foto> arquivos) {
        this.arquivos = arquivos;
    }

}
