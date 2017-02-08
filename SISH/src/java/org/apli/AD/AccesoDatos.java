package org.apli.AD;

import java.io.Serializable;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;



/**
 * Clase que realiza el acceso a la base de datos.
 * Driver, sUrl, sUsr, sPwd están fijos, pero deben leerse de un archivo encriptado
 * Autor: Beatriz A. Olivares Zepahua
 * Fecha: Enero 2004
 */
public final class AccesoDatos implements Serializable{

    private static String sUrl = null;  // sUrl de conexión
    private static String sUsr = null; // Usuario
    private static String sPwd = null;  // Contraseña
    private java.sql.Connection oConexion;  // La conexión
    private int nIDAgregado = 0; 
    
   public AccesoDatos() throws Exception {
        sUrl = "jdbc:postgresql://localhost/SISH";
        sUsr = "usrbdsh";
        sPwd = "usrbdsh1";
        //sUsr = "postgres";
        //sPwd = "Password123";
    }

    /**
     * Realiza la conexión a la base de datos.
     */
    public void conectar() throws Exception {
		
        try 
		{
            Class.forName ("org.postgresql.Driver").newInstance();
            oConexion = DriverManager.getConnection(sUrl, sUsr, sPwd);
        } catch(SQLException e) {
            throw e;
        }
    }

    /**
     * Realiza la desconexión a la base de datos.
     */
    public void desconectar() throws Exception {
		
        oConexion.close();
    }

    /**
     * Código que se ejecuta cuando este objeto es colectado.
     */
    @Override
    public void finalize() throws Exception{
        oConexion.close();
        oConexion = null;
    }
        
    /**
     * Realiza una consulta a la base de datos y retorna un vector de resultados.
     */
    public synchronized Vector ejecutarConsulta(String psQuery) throws Exception {
        
        Statement stmt = null;
        ResultSet rset = null;
        Vector vrset = null;
        ResultSetMetaData rsmd = null;
        int nNumCols = 0;
        try {
            stmt = oConexion.createStatement();
            rset = stmt.executeQuery (psQuery);
            rsmd = rset.getMetaData();
            nNumCols = rsmd.getColumnCount();
            vrset = convierteAVector(rset, rsmd, nNumCols);
        } catch(Exception e){
            if (e.getMessage().contains("Multiple ResultSets"))
                System.out.println("Varios resultsets");
            else
                e.printStackTrace();
            if  (rset != null){
                rset.close();
	    }
            rset = null;
            stmt = null;
            vrset=new Vector();
            try{
                vrset.addElement(" "+e.getMessage());
            }catch(Exception e6){
                e6.printStackTrace();
            }
            
        } finally{
            if(rset != null){
                rset.close();
                if (stmt != null)
                    stmt.close(); 
             }
            rset = null;
            stmt = null;
        }
        return vrset;
    }
         
    /**
     * Realiza una petición de modificación de datos, retornando
     * un int con el número de registros afectados.
     */
    public synchronized int ejecutarComando(String psStatement) 
            throws Exception {
 		
        int ret = 0;
        Vector vTransaction = new Vector();
        
        vTransaction.addElement(psStatement);
        ret = ejecutarComando(vTransaction);
 
        return ret;     
    }
        
    /**
     * Realiza una serie de peticiones de modificación de datos, retornando
     * un int con el número de registros afectados.
     * Estas peticiones son ejecutadas todas en una transacción.
     */
    public synchronized int ejecutarComando(Vector pvStatement) 
            throws Exception {

        int ret = 0, i=0;
        Statement stmt = null;
        String temp = "";
        
        try {
            oConexion.setAutoCommit(false); 
            stmt = oConexion.createStatement();
            for (i=0; i < pvStatement.size(); i++) {
                temp = (String)pvStatement.elementAt(i);
                ret += stmt.executeUpdate(temp);
            }
            oConexion.commit();
        } catch(SQLException e) {
            oConexion.rollback();
            throw e;
        } finally {
            if (stmt != null)
                stmt.close();
            stmt = null;
        }
 
        return ret;
    }
        
    /**
     * Recorre un result set y entrega el vector resultante.
     */
    private synchronized Vector convierteAVector( ResultSet rset, 
                                              ResultSetMetaData rsmd,
                                              int nNumCols ) 
            throws Exception {
        Vector vrset = new Vector();
        Vector vrsettmp = null;
        int i=0;
        
        while (rset.next()) {
            vrsettmp = new Vector();
            for (i = 1; i <= nNumCols; i++) {   

                switch (rsmd.getColumnType(i)) {
                case Types.CHAR:
                case Types.VARCHAR:
                    String varchar = "" + doubleQuote(rset.getString(i));
                    vrsettmp.addElement(varchar);
                    break;
                case Types.INTEGER:
                    vrsettmp.addElement(new Double(rset.getLong(i)));
                    break;
                case Types.SMALLINT:
                    vrsettmp.addElement(new Double(rset.getInt(i)));             
                    break;
                case Types.BIGINT:
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.DOUBLE:
                    vrsettmp.addElement(new Double(rset.getDouble(i)));
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    vrsettmp.addElement((rset.getTimestamp(i)==null?null:new Date(rset.getTimestamp(i).getTime())));
                    break;
               default:
                    String str = "" + rset.getString(i);
                    vrsettmp.addElement(str);
                } //switch  
            }  //for
            vrset.addElement(vrsettmp);
        } //while
        return vrset;
    }
        
    /**
     * Imprime en forma adecuada este objeto.
     * @return String los datos del objeto.
     */
    @Override
    public String toString() {
        String s = "Class = DataAccess \n"+
                "    static sUrl  = " + sUrl + "\n"+
                "    static sUsr = " + sUsr + "\n"+
                "    static sPwd  = " + sPwd + "\n"+
                "    oConexion = " + oConexion + "\n";
        return s;
    }
    
    /**
     * Si la cadena contiene comillas en la base de datos, convierte a código.
     * @return String cadena sin las comillas internas.
     */
	private String doubleQuote(String psCadena){
		if(psCadena == null){
			psCadena = "";
		}
		String CadenaEntrada="";
		if(psCadena.equals("")){
			return psCadena;
		} else if(psCadena.equals("\"")){
			return "&quot;";
		} else {
			int indice = -2;
			CadenaEntrada=psCadena;
			while((indice = CadenaEntrada.indexOf("\"",indice+2))!=-1)
				CadenaEntrada=CadenaEntrada.substring(0,CadenaEntrada.indexOf("\"",indice))+"&quot;"+CadenaEntrada.substring(CadenaEntrada.indexOf("\"",indice)+1);
		}
		return CadenaEntrada;
	}
	
	public String createArray(String lista[][], String clase) throws SQLException{
            Array arr=oConexion.createArrayOf("varchar", lista);
            CallableStatement cstmt;
            if (clase.equals("pf"))
                 cstmt= oConexion.prepareCall("{? = call actualizaRelPF(?)}"); 
            else
                 cstmt = oConexion.prepareCall("{? = call actualizaRelPU(?)}"); 
            cstmt.registerOutParameter(1, 12);
            try { 
                cstmt.setArray(2, arr); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
            cstmt.execute(); 
            String result = cstmt.getString(1); 
            return result;
        }
        
        public String arrayMAPac(String lista[][]) throws SQLException{
            Array arr=oConexion.createArrayOf("varchar", lista);
            CallableStatement cstmt;
            cstmt = oConexion.prepareCall("{? = call actualizaMAPac(?)}"); 
            cstmt.registerOutParameter(1, 12);
            try { 
                cstmt.setArray(2, arr); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
            cstmt.execute(); 
            String result = cstmt.getString(1); 
            return result;
        }
}