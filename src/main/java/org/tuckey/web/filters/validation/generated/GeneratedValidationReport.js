var vfrDisplayed = true;
function vfrToggle() {
    document.getElementById('validationFilterReport').style.display = vfrDisplayed ? 'none' : '';
    vfrDisplayed = !vfrDisplayed;
}
function vfrHide() {
    document.getElementById('validationFilterReport').style.display = 'none';
    document.getElementById('validationFilterReportControl').style.display = 'none';
}
