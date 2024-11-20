function solicitarCertificado(button) {
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    var solicitud_id = fila.querySelector('td').textContent.trim();
    
    // Mostrar modal de confirmación con SweetAlert
    Swal.fire({
        title: '¿Estás seguro?',
        text: `Iniciar proceso?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, solicitar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Si el usuario confirma, se envía el formulario
            enviarFormularioProceso('/solicitar_certificado', solicitud_id);
        }
    });
}

// Función reutilizable para enviar el formulario
function enviarFormularioProceso(actionUrl, solicitud_id) {
    var form = document.createElement('form');
    form.method = 'post';
    form.action = actionUrl;

    var solicitudIdInput = document.createElement('input');
    solicitudIdInput.type = 'hidden';
    solicitudIdInput.name = 'solicitud_id';
    solicitudIdInput.value = solicitud_id;

    // Agregar los inputs al formulario
    form.appendChild(solicitudIdInput);

    document.body.appendChild(form);
    form.submit();
}

function descargarCertificado(button) {
    // Obtener la URL del archivo desde el atributo data-url
    const fileUrl = button.getAttribute('data-url');
    
    if (fileUrl) {
        // Crear un enlace temporal para la descarga
        const link = document.createElement('a');
        link.href = fileUrl;
        link.download = ''; // Puedes agregar un nombre sugerido para el archivo
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } else {
        alert('No se encontró el archivo para descargar.');
    }
}