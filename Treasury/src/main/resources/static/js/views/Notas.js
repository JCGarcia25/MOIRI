document.getElementById("downloadPng").addEventListener("click", function () {
    const table = document.getElementById("notasTable");
    html2canvas(table).then((canvas) => {
        const link = document.createElement("a");
        link.download = "tabla_notas.png";
        link.href = canvas.toDataURL();
        link.click();
    });
});