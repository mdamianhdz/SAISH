document.ready = function(){
    console.info("Lanzando ajaxStatus");
    statusDialog.show();
    console.info("ajaxStatus se ha lanzado");
};
window.onload = function(){
    console.info("Preparando cierre de ajaxStatus");
    statusDialog.hide();
    console.info("ajaxStatus se ha cerrado");
};