package org.apli.jbs.utilidades;
import java.nio.charset.Charset;
/**
 * Clase para solucionar problema de cambio de encoding de PF
 * y que afecta sobre todo a facturación
 * @author BAOZ
 */
public class StandardCharsets {
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
}
