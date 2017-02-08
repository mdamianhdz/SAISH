package org.apli.jbs.utilidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.Usuario;
import org.apli.modelbeans.Utilidades;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author ERJI
 */
@ManagedBean(name="oMenu")
@SessionScoped
public class MenuJB implements Serializable {

private List lista=new ArrayList();
private String padre="";
private String hijo="";
private String guardaElimina="";
private String desactivado="true";
private String nombre="";
private List<Utilidades> listMenu;
private List<String> listMenus;
private List<String> listMenus2;
private MenuModel model;
private Utilidades utilidades=new Utilidades();
    
    public MenuJB() throws Exception{
        if(new UsuarioJB().getCargaMenu()==true){
            String sPaginaLlamada=FacesContext.getCurrentInstance().getViewRoot().getViewId();
            if(sPaginaLlamada.equals("/login/menuOpe.xhtml")){
                actualizaModelMenu();          
            }
        }
    }
    
    public void actualizaModelMenu() throws Exception{                                
        model = new DefaultMenuModel();  
          
        if(new UsuarioJB().getSesionUsuario()!=null){
            Usuario usuario=new UsuarioJB().getSesionUsuario();
            String nombreMenu;
            for(int i=0;i<usuario.getListMenu().size();i++){               
                nombreMenu=((Usuario)usuario.getListMenu().get(i)).getMenu();
                
                DefaultMenuItem item = new DefaultMenuItem("External");
                DefaultSubMenu menu = new DefaultSubMenu(nombreMenu);
                
                for(int j=i;j<usuario.getListMenu().size();j++){                        
                    if(nombreMenu.equals(((Usuario)usuario.getListMenu().get(j)).getMenu())){
                        if(((Usuario)usuario.getListMenu().get(j)).getsSubMenu().equals("")){
                            item = new DefaultMenuItem(((Usuario)usuario.getListMenu().get(j)).getFuncion());  
                            item.setIcon("ui-icon-disk");  
                            item.setCommand(((Usuario)usuario.getListMenu().get(j)).getUrl()+".xhtml?faces-redirect=true");
                            item.setUpdate("messages");  
                            menu.addElement(item);
                        }else{
                            String nombreSubMenu=((Usuario)usuario.getListMenu().get(j)).getsSubMenu();
                            DefaultSubMenu submenu = new DefaultSubMenu(nombreSubMenu);

                            for(int k=j;k<usuario.getListMenu().size();k++){                                    
                                if(nombreSubMenu.equals(((Usuario)usuario.getListMenu().get(k)).getsSubMenu())){                                          
                                   item = new DefaultMenuItem(((Usuario)usuario.getListMenu().get(k)).getFuncion());  
                                   item.setIcon("ui-icon-disk");  
                                   item.setCommand(((Usuario)usuario.getListMenu().get(k)).getUrl()+".xhtml?faces-redirect=true");
                                   item.setUpdate("messages");  
                                   submenu.addElement(item);

                                   if(k==usuario.getListMenu().size()-1){
                                       menu.addElement(submenu);
                                       j=k;
                                   }                                                
                                }else{
                                   j=k-1;
                                   k=usuario.getListMenu().size();
                                   menu.addElement(submenu);
                                }                                    
                            }                                
                        }
                        
                        if(j==usuario.getListMenu().size()-1)
                            i=usuario.getListMenu().size();
                    }else{
                        i=j-1;
                        j=usuario.getListMenu().size();
                    }                    
                }
                model.addElement(menu);
            }
        } 
        actualizaListaMenu();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().
        getSession(false);
        session.setAttribute("modelMenu",model);        
    }
  
    public MenuModel getModel() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().getSession(true);
        return (MenuModel)session.getAttribute("modelMenu");
    }
    
    public void actualizaListaMenu() throws Exception{
        listMenu=new Utilidades().todasOpcionesMenu();
        listMenus=new ArrayList();
        listMenus2=new ArrayList();
        for(int i=0;i<listMenu.size();i++){
            if(listMenu.get(i).getPadreMenu().equals("Menu")){
                listMenus.add(""+ listMenu.get(i).getMenu());
            }
        }
        listMenus2.addAll(listMenus);
        listMenus2.add("Menu principal");
    }
    
    public List getOptiones(){
    List listOpciones= new ArrayList<String>();
        listOpciones.add("Menu");
        listOpciones.add("SubMenu");
        return listOpciones;
    }
    
    public List<Utilidades> getOpcionesMenu() {  
        return listMenu;
    }
    
    public List<String> getOpcionesMenu2() {  
        
        return listMenus2;
    }
    
    public void insertar() throws Exception{
    String rst="";  
        try{
            if(padre.equals("Menu")){
                utilidades.setPadreMenu("0");
            }

            rst=utilidades.insertaElementoMenu();
            if(rst.indexOf("ERROR")>0){
                FacesMessage msj2= new FacesMessage(rst,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
            else{
                utilidades=new Utilidades();
                desactivado="true";
                FacesContext context= FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(rst,rst));
                context.getExternalContext().getFlash().setKeepMessages(true);
                context.getExternalContext().redirect("admMenu.xhtml");
            }           
        }catch(Exception e){
            rst=utilidades.insertaElementoMenu();
            FacesMessage msj2= new FacesMessage(rst,rst);
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            e.printStackTrace();
        }
        actualizaListaMenu();
        utilidades=new Utilidades(); 
   }
    
    public void actualiza(RowEditEvent event) throws Exception{
    String rst="";
        try{
            rst=((Utilidades) event.getObject()).modificarElementoMenu();
            if(rst.indexOf("ERROR")>0){
                FacesMessage msj2= new FacesMessage(rst,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
            else{
                FacesContext context= FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(rst,((Utilidades) event.getObject()).getMenu()));
                context.getExternalContext().getFlash().setKeepMessages(true);
                context.getExternalContext().redirect("admMenu.xhtml");
            }
       }catch(Exception e){
            rst=utilidades.insertaElementoMenu();
            FacesMessage msj2= new FacesMessage(rst,"");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            e.printStackTrace();
       }
        actualizaListaMenu();
        utilidades=new Utilidades();         
   }
    
    public void elimina(String elimina) throws Exception{
    String rst=""; 
        try{
            utilidades.setMenu(elimina);
            rst=utilidades.eliminarElementoMenu();
            if(rst.indexOf("ERROR")>0){
                FacesMessage msj2= new FacesMessage(rst,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
            else{
                FacesContext context= FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(utilidades.getMenu(),rst));
                context.getExternalContext().getFlash().setKeepMessages(true);
                context.getExternalContext().redirect("admMenu.xhtml");
            }           
        }catch(Exception e){
           rst=utilidades.eliminarElementoMenu();
           FacesMessage msj2= new FacesMessage(rst,"");
           FacesContext.getCurrentInstance().addMessage(null, msj2);
           e.printStackTrace();
        }               
        actualizaListaMenu();
        utilidades=new Utilidades();          
   }
    
    public void cancela(){
        
    }
    
    public void valida(){
        if(padre.equals("Menu")){
            lista=new ArrayList();
            desactivado="true";
        }else{
            lista=listMenus;
            desactivado="false";
        }
    }
    
    public void valida(String p){
        if(p.equals("Menu")){
            lista=new ArrayList();
            desactivado="true";
        }else{
            lista=listMenus;
            desactivado="false";
        }
    }
    
    public void limpia(){
        utilidades=new Utilidades();     
        desactivado="true";
        padre="";
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getHijo() {
        return hijo;
    }

    public void setHijo(String hijo) {
        this.hijo = hijo;
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }

    public String getDesactivado() {
        return desactivado;
    }

    public void setDesactivado(String desactivado) {
        this.desactivado = desactivado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Utilidades getUtilidades() {
        return utilidades;
    }

    public void setUtilidades(Utilidades utilidades) {
        this.utilidades = utilidades;
    }
    
    public void guardaElimina(String elimina) {
        this.guardaElimina = elimina;
    }
}
