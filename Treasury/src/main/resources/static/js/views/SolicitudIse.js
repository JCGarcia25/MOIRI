var solicitud_id;

function consultarNotas(button) {
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    var solicitud_id = fila.querySelector('td').textContent.trim();
    
    // Mostrar modal de confirmación con SweetAlert
    Swal.fire({
        title: '¿Estás seguro?',
        text: `Consultar Notas?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, solicitar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Si el usuario confirma, se envía el formulario
            enviarFormularioProceso('/consultar_informacion_estudiante', solicitud_id);
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

function guardarSolicitudId(button) {
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    solicitud_id = fila.querySelector('td').textContent.trim();
 
    document.getElementById('solicitud_id').value = solicitud_id;
}

function alertas(event) {
    event.preventDefault(); // Evita la recarga de la página

    const form = event.target; // Obtén el formulario
    const formData = new FormData(form); // Crea un objeto FormData con los datos del formulario

    // Muestra un mensaje de carga
    Swal.fire({
        position: 'center',
        icon: 'info',
        title: 'Sending response...',
        timerProgressBar: true,
        showConfirmButton: false,
        timer: 2500
    });

    // Envía los datos al servidor usando fetch
    fetch('/subir_certificado', {
        method: 'POST',
        body: formData
    })
        .then((response) => {
            if (response.ok) {
                return response.text(); // Si el servidor responde correctamente
            } else {
                throw new Error('Failed to upload the report');
            }
        })
        .then((result) => {
            // Muestra mensaje de éxito
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Successful sending!',
                text: 'The report has been successfully uploaded.',
                showConfirmButton: false,
                timer: 2000
            }).then(() => {
                // Opcional: redirige o actualiza manualmente la página
                window.location.reload(); // O especifica una URL para redireccionar
            });
        })
        .catch((error) => {
            // Muestra mensaje de error
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Failed to send!',
                text: error.message,
                showConfirmButton: true
            });
        });

    return false; // Evita el envío del formulario
}

function validatePdfs(inputElement) {
    if (!inputElement.value.trim()) {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
        return false;
    } else {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
        return true;
    }
}