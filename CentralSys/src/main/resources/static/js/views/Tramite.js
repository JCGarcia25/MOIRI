function solicitarEquivalencia(button) {

    var form = document.createElement('form');
    form.method = 'post';
    form.action = '/solicitar_equivalencia';
    
    var fila = button.closest('tr');
    
    // Obtén el valor de la primera celda (ID de la solicitud)
    var tramite_id = fila.querySelector('td').textContent.trim();
 
    // Crear los inputs ocultos y agregarlos al formulario
    var solicitudIdInput = document.createElement('input');
    solicitudIdInput.type = 'hidden';
    solicitudIdInput.name = 'tramite_id'; // El nombre debe coincidir con el controlador
    solicitudIdInput.value = tramite_id; // Obtiene el valor del input existente

    // Agregar los inputs al formulario
    form.appendChild(solicitudIdInput);

    document.body.appendChild(form);
    form.submit();
}