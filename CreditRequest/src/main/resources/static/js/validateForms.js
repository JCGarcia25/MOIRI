document.addEventListener("DOMContentLoaded", function () {
    // Obtén el formulario por su ID o cualquier otro selector
    const form = document.getElementById('formMensual'); // Reemplaza 'personForm' con el ID de tu formulario

    // Obtén todos los elementos input dentro del formulario
    const inputElements = form.querySelectorAll('input, select');

    // Itera a través de los elementos input
    inputElements.forEach((inputElement) => {
        // Verifica si el input no está deshabilitado
        if (!inputElement.disabled) {
            const valorInput = inputElement.value;

            // Verifica si el input tiene un valor y agrega la clase 'is-valid' si es necesario
            if (valorInput.trim() !== '') {
                inputElement.classList.add('is-valid');
            }
        }
    });

    const disabledInputsWithValue = document.querySelectorAll('input[disabled][value]');

    disabledInputsWithValue.forEach(function (input) {
        input.classList.add('is-valid');
    });
});

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
