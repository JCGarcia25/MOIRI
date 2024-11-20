function proceso(button) {
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    var estudiante_id = fila.querySelector('td').textContent.trim();
    
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
            enviarFormularioProceso('/proceso', estudiante_id);
        }
    });
}

// Función reutilizable para enviar el formulario
function enviarFormularioProceso(actionUrl, estudiante_id) {
    var form = document.createElement('form');
    form.method = 'post';
    form.action = actionUrl;

    var estudianteIdInput = document.createElement('input');
    estudianteIdInput.type = 'hidden';
    estudianteIdInput.name = 'estudiante_id';
    estudianteIdInput.value = estudiante_id;

    // Agregar los inputs al formulario
    form.appendChild(estudianteIdInput);

    document.body.appendChild(form);
    form.submit();
}

function enviarFormulario(actionUrl, estudiante_id) {
    var form = document.createElement('form');
    form.method = 'post';
    form.action = actionUrl;
    
    var estudianteIdInput = document.createElement('input');
    estudianteIdInput.type = 'hidden';
    estudianteIdInput.name = 'estudiante_id';
    estudianteIdInput.value = estudiante_id;

    // Agregar los inputs al formulario
    form.appendChild(estudianteIdInput);

    document.body.appendChild(form);
    form.submit();
}

function mensual(button) {

    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    var estudiante_id = fila.querySelector('td').textContent.trim();
    
    // Mostrar modal de confirmación con SweetAlert
    Swal.fire({
        title: '¿Estás seguro?',
        text: `Solicitar reporte mensual?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, solicitar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Si el usuario confirma, se envía el formulario
            enviarFormulario('/reporte_mensual', estudiante_id);
        }
    });
}

function final(button) {
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    var estudiante_id = fila.querySelector('td').textContent.trim();
    // Mostrar modal de confirmación con SweetAlert
    Swal.fire({
        title: '¿Estás seguro?',
        text: `Solicitar reporte final?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, solicitar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Si el usuario confirma, se envía el formulario
            enviarFormulario('/reporte_final', estudiante_id);
        }
    });
}
