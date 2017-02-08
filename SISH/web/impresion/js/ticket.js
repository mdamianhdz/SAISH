deployQZ();
function deployQZ() {
    var attributes = {id: "qz", code: 'qz.PrintApplet.class',
        archive: 'qz-print.jar', width: 1, height: 1};
    var parameters = {jnlp_href: 'qz-print_jnlp.jnlp',
        cache_option: 'plugin', disable_logging: 'false',
        initial_focus: 'false'};
    if (deployJava.versionCheck("1.7+") === true) {
    }
    else if (deployJava.versionCheck("1.6+") === true) {
        attributes['archive'] = 'jre6/qz-print.jar';
        parameters['jnlp_href'] = 'jre6/qz-print_jnlp.jnlp';
    }
    deployJava.runApplet(attributes, parameters, '1.5');
}

/**
 * Automatically gets called when applet has loaded.
 */
function qzReady() {
    // Setup our global qz object
    window["qz"] = document.getElementById('qz');
    if (qz) {
        try {
            console.info("Configuraciones realizadas correctamente. Esta listo para imprimir!.");
            readyToPrint();
        } catch (err) { // LiveConnect error, display a detailed meesage
            console.info(err);
            changeState("error");
            console.info("ERROR:  \nThe applet did not load correctly.  Communication to the " +
                    "applet has failed, likely caused by Java Security Settings.  \n\n" +
                    "CAUSE:  \nJava 7 update 25 and higher block LiveConnect calls " +
                    "once Oracle has marked that version as outdated, which " +
                    "is likely the cause.  \n\nSOLUTION:  \n  1. Update Java to the latest " +
                    "Java version \n          (or)\n  2. Lower the security " +
                    "settings from the Java Control Panel.");
        }
    }
}

/**
 * Returns whether or not the applet is not ready to print.
 * Displays an alert if not ready.
 */
function notReady() {
    // If applet is not loaded, display an error
    if (!isLoaded()) {
        return true;
    }
    // If a printer hasn't been selected, display a message.
    else if (!qz.getPrinter()) {
        console.info('Seleccion una impresora antes de enviar a imprimir.');
        changeState("error");
        return true;
    }
    return false;
}

/**
 * Returns is the applet is not loaded properly
 */
function isLoaded() {
    if (!qz) {
        console.info('Error:\n\n\tComplemento para impresion no esta cargado!');
        changeState("error");
        return false;
    } else {
        try {
            if (!qz.isActive()) {
                console.info('Error:\n\n\tComplemento para impresion esta cargado pero no esta activo!');
                changeState("error");
                return false;
            }
        } catch (err) {
            console.info('Error:\n\n\tComplemento para impresion no se ha cargado correctamente!');
            changeState("error");
            return false;
        }
    }
    return true;
}

/**
 * Automatically gets called when "qz.print()" is finished.
 */
function qzDonePrinting() {
    // Alert error, if any
    if (qz.getException()) {
        console.info('Error imprimiendo:\n\n\t' + qz.getException().getLocalizedMessage());
        changeState("error");
        qz.clearException();
        return;
    }

    // Alert success message
    console.info('Se ha enviado correctamente la informacion a la impresora "' + qz.getPrinter());
}

/***************************************************************************
 * Prototype function for finding the "default printer" on the system
 * Usage:
 *    qz.findPrinter();
 *    window['qzDoneFinding'] = function() { alert(qz.getPrinter()); };
 ***************************************************************************/
function useDefaultPrinter() {

    if (isLoaded()) {
        // Searches for default printer
        qz.findPrinter();
        // Automatically gets called when "qz.findPrinter()" is finished.
        window['qzDoneFinding'] = function() {
            // Alert the printer name to user
            var printer = qz.getPrinter();
            console.info(printer !== null ? 'Impresora predeterminada encontrada: "' + printer + '"' :
                    'Impresora predeterminada ' + 'no encontrada');
            return true;
            // Remove reference to this function
            window['qzDoneFinding'] = null;
        };
    }
    return false;
}

function findPrinter(name) {
    // Get printer name from input box
    var p = document.getElementById('printer');
    if (name) {
        p.value = name;
    }

    if (isLoaded()) {
        // Searches for locally installed printer with specified name
        qz.findPrinter(p.value);
        // Automatically gets called when "qz.findPrinter()" is finished.
        window['qzDoneFinding'] = function() {
            var p = document.getElementById('printer');
            var printer = qz.getPrinter();
            // Alert the printer name to user
            console.info(printer !== null ? 'Printer found: "' + printer +
                    '" after searching for "' + p.value + '"' : 'Printer "' +
                    p.value + '" not found.');
            // Remove reference to this function
            window['qzDoneFinding'] = null;
        };
    }
}

function printTicket() {
    changeState("printing");
    if (notReady()) {
        console.info("notReady()");
        changeState("error");
        return;
    }
    qz.setEncoding("850");
    // Hint:  Carriage Return = \r, New Line = \n, Escape Double Quotes= \"
    if (data.reimpresion) {
        qz.append('    ========= REIMPRESION ==========    \n\n');
    }
    qz.append(titles[data.title] + '\n');
    qz.append('     SANATORIO HUERTA  S.A. DE C.V. \n');
    qz.append('      CALLE 11 NO. 231 COL. CENTRO  \n');
    qz.append('        CORDOBA, VER. CP. 94500    \n');
    qz.append('           RFC: SHU930116-L51       \n');
    qz.append('     FECHA: ' + data.fecha + ' ' + data.hora + '\n');
    qz.append('----------------------------------------\n');
    if (data.title === 1) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Folio paciente: ' + data.paciente.folio + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Empresa: ' + printComplete("emp", replaceAccents(data.empresa)));
        qz.append('Dr. solicita: ' + printComplete("dr sol", replaceAccents(data.doctor)));
        qz.append('Forma de pago: ' + replaceAccents(data.fmapago) + '\n');
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                     Subtotal: ' + printCompleteNum(formatPrice(data.subtotal)));
        qz.append('                          IVA: ' + printCompleteNum(formatPrice(data.iva)));
        qz.append('                   Descuentos: ' + printCompleteNum(formatPrice(data.desc)));
        qz.append('                        Total: ' + printCompleteNum(formatPrice(data.total)));
        qz.append('\n\n');
        qz.append(data.mensaje);
    } else if (data.title === 2) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Folio hospitalizacion: ' + data.foliohosp + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Empresa: ' + printComplete("emp", replaceAccents(data.empresa)));
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                     Subtotal: ' + printCompleteNum(formatPrice(data.subtotal)));
        qz.append('                          IVA: ' + printCompleteNum(formatPrice(data.iva)));
        qz.append('                        Total: ' + printCompleteNum(formatPrice(data.total)));
        qz.append('\n\n');
        qz.append(data.mensaje);
    } else if (data.title === 3) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Folio hospitalizacion: ' + data.foliohosp + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Forma de pago: ' + replaceAccents(data.fmapago) + '\n');
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                     Subtotal: ' + printCompleteNum(formatPrice(data.subtotal)));
        qz.append('                   Descuentos: ' + printCompleteNum(formatPrice(data.desc)));
        qz.append('                        Total: ' + printCompleteNum(formatPrice(data.total)));
    } else if (data.title === 4) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Folio hospitalizacion: ' + data.foliohosp + '\n');
        qz.append('Fecha ingreso: ' + data.fingreso + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Empresa: ' + printComplete("emp", replaceAccents(data.empresa)));
        qz.append('Forma de pago: ' + replaceAccents(data.fmapago) + '\n');
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                     Subtotal: ' + printCompleteNum(formatPrice(data.subtotal)));
        qz.append('                   Descuentos: ' + printCompleteNum(formatPrice(data.desc)));
        qz.append('                        Total: ' + printCompleteNum(formatPrice(data.total)));
        qz.append('                         Debe: ' + printCompleteNum(formatPrice(data.debe)));
    } else if (data.title === 5) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Arrendatario: ' + printComplete("dr", replaceAccents(data.doctor)));
        qz.append('Rec fiscal: ' + data.recfiscal + '\n');
        qz.append('Num. mensualidad: ' + replaceAccents(data.mes) + '\n');
        qz.append('Pago programado: ' + data.anio + '\n');
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
    } else if (data.title === 6) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Folio paciente: ' + data.paciente.folio + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('PAQUETE: ' + replaceAccents(data.paquete) + '\n');
        qz.append('----------------------------------------\n');
        qz.append('                      Importe: ' + printCompleteNum(formatPrice(data.importe)));
        qz.append('                         Debe: ' + printCompleteNum(formatPrice(data.debe)));
    } else if (data.title === 7) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Folio contrato: ' + data.foliocontrato + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Paquete:' + printComplete("paq", replaceAccents(data.paquete)));
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                     Subtotal: ' + printCompleteNum(formatPrice(data.subtotal)));
        qz.append('                          IVA: ' + printCompleteNum(formatPrice(data.iva)));
        qz.append('                        Total: ' + printCompleteNum(formatPrice(data.total)));
    } else if (data.title === 8) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Forma de pago: ' + replaceAccents(data.fmapago) + '\n');
        qz.append('Factura: ' + data.factura + '\n');
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                       Cuenta: ' + printCompleteNum(formatPrice(data.cuenta)));
        qz.append('                       Abonos: ' + printCompleteNum(formatPrice(data.abonos)));
        qz.append('                         Pago: ' + printCompleteNum(formatPrice(data.pago)));
        qz.append('                   Descuentos: ' + printCompleteNum(formatPrice(data.desc)));
        qz.append('                         Debe: ' + printCompleteNum(formatPrice(data.debe)));
    } else if (data.title === 9) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('Paquete: ' + printComplete("paq", replaceAccents(data.paquete)));
        qz.append('Forma de pago: ' + replaceAccents(data.fmapago) + '\n');
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                       Cuenta: ' + printCompleteNum(formatPrice(data.cuenta)));
        qz.append('                       Abonos: ' + printCompleteNum(formatPrice(data.abonos)));
        qz.append('                         Pago: ' + printCompleteNum(formatPrice(data.pago)));
        qz.append('                         Debe: ' + printCompleteNum(formatPrice(data.debe)));
    } else if (data.title === 10) {
        qz.append('Prestamo: ' + data.prestamo + '\n');
        qz.append('Folio egreso: ' + data.folioegreso + '\n');
        qz.append('Folio pago: ' + data.foliopago + '\n');
        qz.append('Personal: ' + printComplete("pers", replaceAccents(data.personal)));
        qz.append('Abono: ' + data.abono + '\n');
        qz.append('Resta: ' + data.resta + '\n');
    } else if (data.title === 11) {
        qz.append('Folio operacion: ' + data.folioope + '\n');
        qz.append('Personal: ' + printComplete("pers", replaceAccents(data.personal)));
        qz.append('Fecha: ' + data.fecha + '\n');
        qz.append('Usuario: ' + printComplete("usr", replaceAccents(data.usuario)));
        qz.append('----------------------------------------\n');
        qz.append(' CONCEPTO                        IMPORTE\n');
        qz.append('----------------------------------------\n');
        qz.append(' ' + data.concepto + '             ' + printCompleteNum(formatPrice(data.importe)));
    }else if (data.title === 12) { //Impresion de ticket de - Orden de Servicio -
        qz.append('Folio orden: ' + data.folioorden + '\n');
        qz.append('Folio paciente: ' + data.paciente.folio + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('                SERVICIOS               \n');
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                Importe Total: ' + printCompleteNum(formatPrice(data.total)));
        qz.append('\n\n');
    }else if (data.title === 13) { //Impresion de ticket de - Vale de farmacia -
        qz.append('Folio de vale: ' + data.folioorden + '\n');
        qz.append('No. requisicion: ' + data.folioorden + '\n');
        qz.append('Folio paciente: ' + data.paciente.folio + '\n');
        qz.append('Paciente: ' + printComplete("pac", replaceAccents(data.paciente.nombre)));
        qz.append('         MATERIAL Y MEDICAMENTO         \n');
        qz.append('----------------------------------------\n');
        qz.append('CANT          CONCEPTO            PRECIO\n');
        qz.append('----------------------------------------\n');
        if (typeof data.servicios !== "undefined" && data.servicios.length > 0) {
            for (var i = 0; i < data.servicios.length; i++) {
                qz.append(printConcepto(data.servicios[i].cantidad, replaceAccents(data.servicios[i].descripcion), formatPrice(data.servicios[i].precio)));
            }
        }
        qz.append('----------------------------------------\n');
        qz.append('                Importe Total: ' + printCompleteNum(formatPrice(data.total)));
        qz.append('\n\n');
    }
    qz.appendImage("");
    qz.append('\n\n\n\n\n\n\n');
    // Automatically gets called when "qz.appendImage()" is finished.
    window['qzDoneAppending'] = function() {
        if (production) {
            qz.print();
        }
        changeState("success");
        // Remove any reference to this function
        window['qzDoneAppending'] = null;
    };
}

function printComplete(type, value) {
    var tab = 0;
    if (type === "pac") {//Paciente
        tab = 10;
    } else if (type === "dr sol") {//Dr. solicita
        tab = 14;
    } else if (type === "usr") {//Usuario
        tab = 9;
    } else if (type === "paq") {//Paquete
        tab = 9;
    } else if (type === "emp") {//Empresa
        tab = 9;
    } else if (type === "dr") {//Doctor
        tab = 8;
    } else if (type === "pers") {//Personal
        tab = 10;
    }
    if (tab + value.length > 40) {
        var valueParcial = value.substring(0, 40 - tab);
        var pUltBco = valueParcial.lastIndexOf(" ");
        var firstValue = value.substring(0, pUltBco).concat("\n");
        firstValue += addSpaces(tab) + value.substring(pUltBco + 1).concat("\n");
        return firstValue;
    } else {
        return value.concat("\n");
    }
}

function printCompleteNum(value) {
    var tabRight = 9;
    return addSpaces(tabRight - value.length).concat(value).concat("\n");
}

function printConcepto(amount, concept, price) {
    var separator = 1;
    var sizeAmount = 4;
    var sizeConcept = 26;
    var tabConcept = sizeAmount + separator;
    var sizePrice = 8;
    var value = "";
    var printedPrice = false;
    value = amount + addSpaces(sizeAmount - amount.toString().length) + addSpaces(separator);
    if (concept.toString().length <= sizeConcept) {
        value += concept.concat(addSpaces(sizeConcept - concept.length)).concat(addSpaces(separator));
    } else {
        var valuePartial;
        var valueTmp;
        var indexStart = 0;
        var indexEnd = sizeConcept;
        var indexTmp = 0;
        var incomplete = true;
        var firstLine = true;
        while (incomplete) {
            valuePartial = concept.substring(indexStart, indexEnd);
            indexTmp = valuePartial.lastIndexOf(" ");
            valueTmp = concept.substring(indexStart, indexStart + indexTmp);
            indexStart += indexTmp + 1; //Se agrega uno por el espacio en blanco que estamos dejando al buscar por medio de el
            indexEnd = indexStart + sizeConcept;
            if (firstLine) {
                value += valueTmp.concat(addSpaces(sizeConcept - valueTmp.toString().length)).concat(addSpaces(separator));
                value += addSpaces(sizePrice - price.length).concat(price).concat("\n").concat(addSpaces(tabConcept));
                firstLine = false;
                printedPrice = true;
            } else {
                value += valueTmp.concat("\n").concat(addSpaces(tabConcept));
            }
            if (indexEnd > concept.length) {//Quiere decir que ya se paso el indice para extraer y solo tenemos que añadir un pequeño fragmento
                incomplete = false;
            }
        }
        value += concept.substring(indexStart).concat("\n");
    }
    if (!printedPrice) {
        value += addSpaces(sizePrice - price.length).concat(price).concat("\n");
    }
    return value;
}

function addSpaces(num) {
    var spaces = "";
    var i = 0;
    for (i = 0; i < num; i++) {
        spaces += " ";
    }
    return spaces;
}

function formatPrice(value) {
    var valueString = value.toString();
    var index = valueString.indexOf(".");
    if (index === -1) {
        return valueString.concat(".00");
    } else {
        if (valueString.substring(index).length === 2) {
            return valueString.concat("0");
        } else if (valueString.substring(index.length === 3)) {
            return valueString;
        }
    }
}

function changeState(type) {
    if (type === "success") {
        document.getElementById("message").innerHTML = "<img src='../resources/images/Button-Ok.png' width='24' height='24' alt='success-loader'/> <span class='msj'>Ticket impreso correctamente!</span>";
    } else if (type === "error") {
        document.getElementById("message").innerHTML = "<img src='../resources/images/error-icon.png' width='24' height='24' alt='error-loader'/> <span class='msj'>Ha ocurrido un error al imprimir ticket!</span>";
    } else if (type === "printing") {
        document.getElementById("message").innerHTML = "<img src='../resources/images/ajax-loader.gif' width='24' height='24' alt='loading-loader'/> <span class='msj'>Imprimiendo ticket...</span>";
    }
}

String.prototype.fakeReplace = function(str, newstr) {
    return this.split(str).join(newstr);
};

function replaceAccents(text) {
    var str = text.toString();
    var acentos = "ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛ";
    var original = "AAAAAEEEEIIIIOOOOUUUU";
    for (var i = 0; i < acentos.length; i++) {
        str = str.split(acentos.charAt(i)).join(original.charAt(i));
    }
    return str;
}

function replace(texto, s1, s2) {
    return texto.split(s1).join(s2);
}

function getPath() {
    var path = window.location.href;
    return path.substring(0, path.lastIndexOf("/")) + "/";
}
function fixHTML(html) {
    return html.replace(/ /g, "&nbsp;").replace(/’/g, "'").replace(/-/g, "&#8209;");
}
function chr(i) {
    return String.fromCharCode(i);
}

function allowMultiple() {
    if (isLoaded()) {
        var multiple = qz.getAllowMultipleInstances();
        qz.allowMultipleInstances(!multiple);
        alert('Allowing of multiple applet instances set to "' + !multiple + '"');
    }
}

function readyToPrint() {
    if (window.name === "") {
        console.info("No se han recibido datos para enviar a la impresora");
        changeState("error");
        alert("No se han recibido datos para enviar a la impresora");
    } else {
        console.info(window.name);
        dataTmp = JSON.parse(window.name);
        data = dataTmp;
        qz.findPrinter();
        window['qzDoneFinding'] = function() {
            printTicket();
            changeState("success");
        };
        console.info(data);
        console.info(data.title);
    }
}

var data;
var production = true;
var titles = ["",
    "     ========= EXTERNOS =========",
    "      ========= CARGOS =========",
    "== PAGO A CUENTA DE HOSPITALIZACION ==",
    "    ====== CIERRE DE CUENTA =====",
    "     ====== PAGO DE RENTA ======",
    "      ==== CONTRATO PAQUETE ====",
    "      ==== SERVICIO PAQUETE ====",
    "       ===== PAGO CREDITO =====",
    "      ===== PAGO PAQUETES =====",
    "     ===== PAGO DE PRESTAMO =====",
    "    ===== PRESTAMO A PERSONAL =====",
    "    ====== ORDEN DE SERVICIO ======   ",
    "     ====== VALE DE FARMACIA ======   "];