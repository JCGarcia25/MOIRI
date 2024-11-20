var solicitud_id;
var solicitud_id2;

function ver_archivos(button) {

    var form = document.createElement('form');
    form.method = 'get';
    form.action = '/ver_anexos';
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    var solicitud_id = fila.querySelector('td').textContent.trim();
 
    // Crear los inputs ocultos y agregarlos al formulario
    var solicitudIdInput = document.createElement('input');
    solicitudIdInput.type = 'hidden';
    solicitudIdInput.name = 'solicitud_id'; // El nombre debe coincidir con el controlador
    solicitudIdInput.value = solicitud_id; // Obtiene el valor del input existente

    // Agregar los inputs al formulario
    form.appendChild(solicitudIdInput);

    document.body.appendChild(form);
    form.submit();
}

function guardarSolicitudId(button) {
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    solicitud_id = fila.querySelector('td').textContent.trim();
    
    console.log(solicitud_id);

    document.getElementById('solicitud_id').value = solicitud_id;
    document.getElementById('solicitud_id2').value = solicitud_id;
}

function submitFormExternally(button) {
    // Obtener referencia al formulario
    var form = document.getElementById("formMensual");

    // Crear los inputs ocultos y agregarlos al formulario
    var solicitudIdInput = document.createElement('input');
    solicitudIdInput.type = 'hidden';
    solicitudIdInput.name = 'solicitud_id'; // El nombre debe coincidir con el controlador
    solicitudIdInput.value = solicitud_id; // Obtiene el valor del input existente

    // Agregar los inputs al formulario
    form.appendChild(solicitudIdInput);
    // Verificar si el formulario existe
    if (form) {
        // Disparar el evento de envío del formulario
        //form.dispatchEvent(new Event("submit"));
        alertas(form);
    }
}

function submitFormExternallyFinal() {
    // Obtener referencia al formulario
    var form = document.getElementById("formFinal");

    

    // Verificar si el formulario existe
    if (form) {
        // Disparar el evento de envío del formulario
        //form.dispatchEvent(new Event("submit"));
        alertasFinal(form);
    }
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
    fetch('/subir_anexo', {
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

function alertasFinal(event) {
    event.preventDefault(); // Evita la recarga de la página

    const form = event.target; // Obtén el formulario
    
    // Recupera el valor del input hidden
    const solicitudIdInput = form.querySelector('input[name="solicitud_id2"]');
    const solicitud_id2 = solicitudIdInput ? solicitudIdInput.value : null;
    
    // Crea el FormData a partir del formulario
    const formData = new FormData(form); // Incluye automáticamente el valor del input hidden

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
    fetch('/subir_anexos', {
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