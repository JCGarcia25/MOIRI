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