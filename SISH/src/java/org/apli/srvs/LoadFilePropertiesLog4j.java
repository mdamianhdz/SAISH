package org.apli.srvs;

import java.io.File;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet que realiza la función de cargar el archivo de propiedades del Logger
 *´para manejo de logs tanto en consola para desarrollo como para un archivo al estar
 * en producción
 * @version 1.0
 * @since 15 de Mayo de 2014
 * @author MiguelAngel
 */
public class LoadFilePropertiesLog4j extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        String log4jFile = config.getInitParameter("log4jPropertiesFile");
        ServletContext context = config.getServletContext();
        log4jFile = context.getRealPath(log4jFile);
        if (new File(log4jFile).isFile()) {
            PropertyConfigurator.configure(log4jFile);
        } else {
            BasicConfigurator.configure();
        }
        super.init(config);
    }
}
