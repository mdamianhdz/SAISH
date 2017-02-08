<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:cfdi="http://www.sat.gob.mx/cfd/3" xmlns:ecc="http://www.sat.gob.mx/ecc" xmlns:psgecfd="http://www.sat.gob.mx/psgecfd" xmlns:divisas="http://www.sat.gob.mx/divisas" xmlns:detallista="http://www.sat.gob.mx/detallista" xmlns:ecb="http://www.sat.gob.mx/ecb" xmlns:implocal="http://www.sat.gob.mx/implocal" xmlns:terceros="http://www.sat.gob.mx/terceros" xmlns:iedu="http://www.sat.gob.mx/iedu" xmlns:ventavehiculos="http://www.sat.gob.mx/ventavehiculos" xmlns:pfic="http://www.sat.gob.mx/pfic" xmlns:tpe="http://www.sat.gob.mx/TuristaPasajeroExtranjero" xmlns:leyendasFisc="http://www.sat.gob.mx/leyendasFiscales">
	<!-- Con el siguiente método se establece que la salida deberá ser en texto -->
	<!-- <xsl:output method="text" version="1.0" encoding="UTF-8" indent="no"/> -->
	<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no"/>
	<!--
		En esta sección se define la inclusión de las plantillas de utilerías para colapsar espacios
	-->
	
	<!-- 
		En esta sección se define la inclusión de las demás plantillas de transformación para 
		la generación de las cadenas originales de los complementos fiscales 
	-->
	
	
	<!-- Aquí iniciamos el procesamiento de la cadena original con su | inicial y el terminador || -->
	<xsl:template match="/">|<xsl:apply-templates select="/cfdi:Comprobante"/>||</xsl:template>
	<!--  Aquí iniciamos el procesamiento de los datos incluidos en el comprobante -->
	<xsl:template match="cfdi:Comprobante">
		<!-- Iniciamos el tratamiento de los atributos de comprobante -->
		
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@descuento"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@TipoCambio"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@Moneda"/>
		</xsl:call-template>
		
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@NumCtaPago"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@FolioFiscalOrig"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@SerieFolioFiscalOrig"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@FechaFolioFiscalOrig"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@MontoFolioFiscalOrig"/>
		</xsl:call-template>
		<!--
			Llamadas para procesar al los sub nodos del comprobante
		-->
		<xsl:apply-templates select="./cfdi:Emisor"/>
		<xsl:apply-templates select="./cfdi:Receptor"/>
		
		
		
	</xsl:template>
	<!-- Manejador de nodos tipo Emisor -->
	<xsl:template match="cfdi:Emisor">
		<!-- Iniciamos el tratamiento de los atributos del Emisor -->
		<xsl:call-template name="Requerido">
			<xsl:with-param name="valor" select="./@rfc"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@nombre"/>
		</xsl:call-template>
		<!--
			Llamadas para procesar al los sub nodos del comprobante
		-->
		<xsl:apply-templates select="./cfdi:DomicilioFiscal"/>
		<xsl:if test="./cfdi:ExpedidoEn">
			<xsl:call-template name="Domicilio">
				<xsl:with-param name="Nodo" select="./cfdi:ExpedidoEn"/>
			</xsl:call-template>
		</xsl:if>
		
	</xsl:template>
	<!-- Manejador de nodos tipo Receptor -->
	<xsl:template match="cfdi:Receptor">
		<!-- Iniciamos el tratamiento de los atributos del Receptor -->
		<xsl:call-template name="Requerido">
			<xsl:with-param name="valor" select="./@rfc"/>
		</xsl:call-template>
		<xsl:call-template name="Opcional">
			<xsl:with-param name="valor" select="./@nombre"/>
		</xsl:call-template>
		
	</xsl:template>
	<!-- Manejador de nodos tipo Conceptos -->
	
</xsl:stylesheet>
