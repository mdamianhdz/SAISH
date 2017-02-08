<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version = '1.0'
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
    xmlns:cfdi='http://www.sat.gob.mx/cfd/3'>
	<xsl:output method = "html" /> 
	<xsl:param name="id" select="."/>
	<xsl:template match="//cfdi:Comprobante">
		<html>
			<body >
				<fieldset>
					<legend>
	  					<font color="black" face="arial" size="2">CFDi - Comprobante Fiscal Digital por Internet	  </font>
					</legend>
	          		<table border="0" bordercolor="blue">
			            <tr>
			              <td class="h2" align="center">
			                <center>
			                  <font color="#0B0B6" face="arial" size="2">
			                  <!--<xsl:value-of select="./@tipoDeComprobante"/>-->
			                  </font>
			                </center>
			              </td>
			            </tr>
			          </table>
			          <table width="100%">
			            	<tr>
			              		<td colspan ="2">
			               			<img src="file:///C|/eclipse-jee-kepler-SR1-win32/workspace/COFAI/src/xslt/formatoImpreso/logo.png"  alt=""></img>
			              		</td>
			            	</tr>
			            	<tr>
			              		<td width="65%">
			                		<fieldset>
			                  			<table width="100%" border="0">
			                    			<legend>DATOS FISCALES</legend>
			                    				<tr width="100%">
				                      					<td>
				                         					<font color="#0B0B6" face="Courier New" size="2"> Serie/Folio:   </font>
				                      					</td>
				                      					<td>
				                        					<font color="#0B0B6" face="Courier New" size="2"><b><xsl:value-of select="./@serie"/><xsl:value-of select="./@folio"/> </b></font>
				                      					</td>               
									                 	<td>
									                        <font color="#0B0B6" face="Courier New" size="2">Tasa de Cambio:</font>
									                     </td>
									                     <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> <b> <xsl:value-of select="./@TipoCambio"/></b></font>
									                     </td>
			                    				</tr>
							                    <tr width="100%">
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Fecha Emisión de Comprobante: </font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2" ><b><xsl:value-of select="./@fecha"/></b></font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Motivo Descuento:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"><b><xsl:value-of select="./@motivoDescuento"/></b></font>
									                      </td>
							                    </tr>
							                    <tr width="100%">
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Tipo de Comprobante:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"><b> <xsl:value-of select="./@tipoDeComprobante"/>  </b></font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Método de Pago:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> <b><xsl:value-of select="./@metodoDePago"/></b></font>
									                      </td>
							                    </tr>
							                    <tr width="100%">
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Certificado Emisor:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"><b><xsl:value-of select="./@noCertificado"/> </b></font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> Forma de Pago:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"><b> <xsl:value-of select="./@formaDePago"/> </b></font>
									                      </td>
							                    </tr>
							                    <tr width="100%">
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Divisa:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> <b> <xsl:value-of select="./@Moneda"/></b></font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Condiciones de pago:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"><b> <xsl:value-of select="./@condicionesDePago"/></b></font>
									                      </td>
							                    </tr>		
							                    <tr width="100%">
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> Numero de Cuenta: </font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> <b><xsl:value-of select="./@NumCtaPago"/></b> </font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Regimen Fiscal:</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> <b><xsl:value-of select="cfdi:Emisor/cfdi:RegimenFiscal/@Regimen"/> </b></font>
									                      </td>
							                    </tr>             
											    <tr width="100%">
										              <td>
									                        <font color="#0B0B6" face="Courier New" size="2">LUGAR DE EXPEDICIÓN :</font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"><b> <xsl:value-of select="./@LugarExpedicion"/> </b></font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"> </font>
									                      </td>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2"><b>  </b> </font>
									                      </td>
									 	 	    </tr>
			                            </table>
	                				</fieldset>
	                            </td>
				              <td width="35%">
				                	<fieldset>
				                  		<table width="100%" border="0">
				                      		<legend>DATOS DE TIMBRADO</legend>             
							                    <tr>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Certificado SAT:<br></br><b><!--              <xsl:value-of select="document('cad.xml')/Complemento/NoCertificadoSAT"/>--></b></font>
									                      </td>
							                    </tr>
							                    <tr>
									                      <td>
									                        <font color="#0B0B6" face="Courier New" size="2">Folio Fiscal:<br></br><b>   <!-- <xsl:value-of select="document('auxiliar.xml')/Complemento/UUID"/>--> </b></font>  
									                      </td> 
							                    </tr>
							                    <tr>
									                      <td>
									                      <font color="#0B0B6" face="Courier New" size="2">Fecha de Certificación del CFDi:<br></br><b> <!--                   <xsl:value-of select="document('cad.xml')/Complemento/FechaTimbrado"/>--> </b> </font>
									                      </td>
							                    </tr>
				                  		</table>
				                	</fieldset>
				              </td>
	            		</tr>
	          </table>
	          <table width="100%">            
	            <tr>
	              <td width="50%">
	                <fieldset border="0">
	                  <table width="100%" border="0">
	                    <legend>
	                      EMISOR
	                    </legend>
	                    <tr width="100%">
	                      <td>
	                        <font color="#0B0B6" face="Courier New" size="2">
	                          RFC :
	                          <b>
	                        <xsl:value-of select="cfdi:Emisor/@rfc"/>
	                            </b>
	                        </font>
	                      </td>
	                    </tr>
	
	                    <tr width="100%">
	                      <td>
	                        <font color="#0B0B6" face="Courier New" size="2">
	                          Nombre :
	                          <b>
	                           <xsl:value-of select="cfdi:Emisor/@nombre"/>
	                            </b>
	                        </font>
	                      </td>
	                    </tr>
	                    <font color="#0B0B6" face="Courier New" size="2">
	                      <b>
	                     <xsl:apply-templates select="//cfdi:DomicilioFiscal"/>
	                        </b>
	                    </font>
	                  </table>
	                </fieldset>
	              </td>
	              <td width="50%">
	                <fieldset>
	                  <table width="100%" border="0">
	                    <legend>
	                      RECEPTOR
	                    </legend>
	                    <tr width ="100%">                
	                      <td width="75%">
	                        <font color="#0B0B6" face="Courier New" size="2">
	                         RFC: <b>
	                         <xsl:value-of select="cfdi:Receptor/@rfc"/>
	                        </b>
	                        </font>
	                      </td>
	                    </tr>
	                    <tr>
	                      <td>
	                        <font color="#0B0B6" face="Courier New" size="2">
	                          Nombre: <b>
	                        <xsl:value-of select="cfdi:Receptor/@nombre"/>
	                        </b>
	                        </font>
	                      </td>
	                    </tr>
	                    <font color="#0B0B6" face="Courier New" size="2">
	                     <b> <xsl:apply-templates select="//cfdi:Domicilio"/>
	                         </b>
	                    </font>
	                  </table>
	                </fieldset>
	              </td>
	            </tr>
	          </table>
	          </fieldset>
				<fieldset>
					<legend>CONCEPTOS</legend>
					<table width="100%" border="0">
						<tr>
							<th>
								<font color="#0B0B6" face="Century Gothic" size="2">Cantidad</font>
							</th>

							<th>
								<font color="#0B0B6" face="Century Gothic" size="2">Unidad</font>
							</th>
							<th>
								<font color="#0B0B6" face="Century Gothic" size="2">Descripcion</font>
							</th>
							<th>
								<font color="#0B0B6" face="Century Gothic" size="2">Precio</font>
									  
							</th>
							<th>
								<font color="#0B0B6" face="Century Gothic" size="2">Importe</font>
							</th>
						</tr>
						<tr>
							<td> </td>
							<td align="left">
								<font color="#0B0B6" face="Courier New" size="2"><PRE>  <b>  <!-- <xsl:value-of select="document('c:/myfacturae/cad.xml')/Complemento/Encabezado"/>--></b></PRE></font>
							</td>
							<td> </td>
							<td align="right"></td>
						</tr>
						<font color="#0B0B6" face="Courier New" size="2">
							<xsl:apply-templates select="//cfdi:Concepto"/>
							<xsl:for-each select="Concepto"></xsl:for-each>
						</font>
						<tr>
							<td> </td>
							<td align="left">
								<font color="#0B0B6" face="Courier New" size="2"><PRE><b></b></PRE></font>
							</td>
							<td> </td>
							<td align="right"></td>
						</tr>
						<table width="100%" border="0" style="background:url('fondoo.jpg') no-repeat bottom">
							<tr>
								<th width="90%"></th>
								<th width="10%"></th>
							</tr>          
							<tr>
								<td align="right">
									<font color="#0B0B6" face="Courier New" size="2"><p style="word-spacing: 2em;">SUBTOTAL: </p></font>
								</td>
								<td align="right" >
									<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select='format-number(@subTotal,"$###,###,###.00")'/></font>
								</td>
							</tr>
							<tr>
								<td align="right">
									<font color="#0B0B6" face="Courier New" size="2"><p style="word-spacing: 2em;">DESCUENTO:</p></font>
								</td>
								<td align="right" >
									<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select='format-number(@descuento,"$###,###,###.00")'/></font>
								</td>
							</tr>         
							<font color="#0B0B6" face="Courier New" size="2">
								<xsl:apply-templates select="//cfdi:Traslado"/>
							</font>
							<font color="#0B0B6" face="Courier New" size="2">
								<xsl:for-each select="Traslado">
								</xsl:for-each>
							</font>
							<font color="#0B0B6" face="Courier New" size="2">
								<xsl:apply-templates select="//cfdi:Retencion"/>
								<xsl:for-each select="Retencion">
								</xsl:for-each>
							</font>
							<tr>
								<td align="right">
									<font color="#0B0B6" face="Courier New" size="2"><b>TOTAL: </b></font>
								</td>
								<td align="right">
									<font color="#0B0B6" face="Courier New" size="2"><b><xsl:value-of select='format-number(@total,"$###,###,###.00")'/></b></font>
								</td>
							</tr>
							<tr>
								<td> </td>
								<td align="left">
									<font color="#0B0B6" face="Courier New" size="2"><b><PRE></PRE></b></font>
								</td>
								<td> </td>
								<td align="right"></td>
							</tr>
						</table>
					</table>
					
					<hr/>
					<table width="100%" border="0">
  <tr>
    <td width="25%" rowspan="10"><center><img src="file:///C|/eclipse-jee-kepler-SR1-win32/workspace/COFAI/src/xslt/formatoImpreso/codigoQR.png" width="200" height="200"  alt=""></img></center></td>
    <td width="75%"><center>
									<font color="#0B0B6" face="Courier New" size="2"><b><pre> E F E C T O S   F I S C A L E S   A L   P A G O </pre></b></font>
								</center></td>
  </tr>
  <tr>
    <th><font color="#0B0B6" face="Courier New" size="2">Cantidad con letra</font></th>
  </tr>
  <tr>
    <td><font color="#0B0B6" face="Courier New" size="2"> <xsl:value-of select="CFDIEmitido/@montoFacturar"/><!-- <xsl:value-of select="document('c:/myfacturae/cad.xml')/Complemento/CantidadLetra"/>--></font></td>
  </tr>
  <tr>
<th>
								<font color="#0B0B6" face="Courier New" size="2">Cadena Original del Complemento de certificación digital del SAT</font>
							</th>
  </tr>
  <tr>
    <td><font color="#0B0B6" face="Courier New" size="3"><pre><b><xsl:value-of select="CFDIEmitido/@cadenaOriginal"/><!-- <xsl:value-of select="document('c:/myfacturae/cad.xml')/Complemento/CadenaTFD"/>--></b></pre></font></td>
  </tr>
  <tr>
<th><font color="#0B0B6" face="Courier New" size="2">Sello Digital Emisor</font></th>
  </tr>
  <tr>
    <td><font color="#0B0B6" face="Courier New" size="3"><pre><b><xsl:value-of select="./@sello"/><!-- <xsl:value-of select="document('c:/myfacturae/cad.xml')/Complemento/SelloCFD"/>--></b></pre></font></td>
  </tr>
  <tr width="100%">
							<th ><font color="#0B0B6" face="Courier New" size="2">Sello Digital SAT</font></th>
						</tr>
  <tr>
    <td width="50%">
								<font color="#0B0B6" face="Courier New" size="3"><pre><b><!-- <xsl:value-of select="document('c:/myfacturae/cad.xml')/Complemento/SelloSAT"/>--></b></pre></font>
							</td>
  </tr>
  <tr>
    <td style="text-align: center"><font color="#0B0B6" face="Courier New" size="2">Este documento es una representación impresa</font></td>
  </tr>
</table>
					
					<br></br>
				</fieldset>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="//cfdi:DomicilioFiscal"> 
    <tr>
      <td colspan="2">
        <font color="#0B0B6" face="Courier New" size="2">
          Domicilio:
          <b>
          <xsl:value-of select="@calle"/><font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@noExterior"/><font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@noInterior"/><font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@colonia"/><font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@municipio"/><font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@estado"/><font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@pais"/><font color="white" face="Courier New" size="2">,</font>
          C.P. <xsl:value-of select="@codigoPostal"/>
        </b>
        </font>
      </td>
    </tr>
  </xsl:template>
  <xsl:template match="//cfdi:Domicilio">
    <tr>
      <td colspan="2">
        <font color="#0B0B6" face="Courier New" size="2">
          Domicilio:
          <b>
          <xsl:value-of select="@calle"/>
            <font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@noExterior"/>
            <font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@noInterior"/>
            <font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@colonia"/>
            <font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@municipio"/>
            <font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@estado"/>
            <font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@pais"/>
            <font color="white" face="Courier New" size="2">,</font>
          <xsl:value-of select="@codigoPostal"/>
        </b>
        </font>
      </td>
    </tr>
  </xsl:template>
<xsl:template match="//cfdi:Concepto">
		<tr>
			<td align="center" width="10%">
				<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select="@cantidad"/></font>
			</td>
			<td align="center" width="10%">
				<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select="@unidad"/></font>
			</td>
			<td width="50%">
				<font color="#0B0B6" face="Courier New" size="2"><pre><xsl:value-of select="@descripcion"/></pre></font>
			</td>
			<td align="right" width="20%">
				<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select='format-number(@valorUnitario,"$###,###,###.00")'/></font>
			</td>
			<td align="right" width="20%">
				<font color="#0B0B6" face="Courier New" size="2"><b><xsl:value-of select='format-number(@importe,"$###,###,###.00")'/></b></font>
			</td>
		</tr>
		<tr width='100%'>
			<td width='100%' colspan='4'>
				<hr></hr>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="//cfdi:Traslado">
		<tr>
			<td align="right">
				<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select="@impuesto"/></font>
			</td>
			<td align="right">
				<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select='format-number(@importe,"$###,###,###.00")' /></font>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="//cfdi:Retencion">
		<tr>
			<td align="right">
				<font color="#0B0B6" face="Courier New" size="2">RET. <xsl:value-of select="@impuesto"/></font>
			</td>
			<td align="right">
				<font color="#0B0B6" face="Courier New" size="2"><xsl:value-of select='format-number(@importe,"$###,###,###.00")'/></font>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>