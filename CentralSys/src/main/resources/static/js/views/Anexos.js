function mostrarPrevisualizacion(button) {
    const url = button.getAttribute('data-url');
    const visor = document.getElementById('visor-pdf');
    const iframe = document.getElementById('iframe-pdf');
    
    if (url) {
        // Crear un enlace temporal para la descarga
        const link = document.createElement('a');
        link.href = url;
        link.download = ''; // Puedes agregar un nombre sugerido para el archivo
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } else {
        alert('No se encontró el archivo para descargar.');
    }

    if (url) {
        iframe.src = url;
        visor.style.display = 'block';
    } else {
        alert('No se pudo cargar el archivo. Verifica el enlace.');
    }
}
