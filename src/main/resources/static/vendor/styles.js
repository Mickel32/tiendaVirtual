document.addEventListener('DOMContentLoaded', function () {
    const container = document.querySelector('.imagen_produ');
    const image = document.querySelector('.image_produ');
    const zoom = document.querySelector('.zoom');



    container.addEventListener('mousemove', function (event) {
        // Obtener la posición del cursor dentro de la imagen
        const bounds = container.getBoundingClientRect();
        const x = event.clientX - bounds.left;
        const y = event.clientY - bounds.top;

        // Calcular las proporciones para la lupa
        const ratioX = image.width / container.offsetWidth;
        const ratioY = image.height / container.offsetHeight;

        // Posicionar la lupa y mostrarla
        const zoomX = -x * ratioX + container.offsetWidth / 20;
        const zoomY = -y * ratioY + container.offsetHeight / 20;
        zoom.style.backgroundImage = `url('${image.src}')`;
        zoom.style.backgroundPosition = `${zoomX}px ${zoomY}px`;
        //zoom.style.display = 'block';
        zoom.style.opacity = '1';
    });

    container.addEventListener('mouseout', function () {
        // Ocultar la lupa cuando el cursor sale de la imagen
        zoom.style.opacity = '0';
    });

});

// Obtenemos todos los elementos "a" con la clase "cambiarColor"
const enlaces = document.querySelectorAll(".empresas a");

// Agregamos un evento de clic a cada enlace dentro del contenedor
enlaces.forEach(enlace => {
    enlace.addEventListener("click", () => {
        // Obtenemos el id del enlace clicado
        const enlaceId = enlace.getAttribute("data-id");

        // Guardamos el id del enlace clicado en localStorage para recordar el estado después de recargar la página
        localStorage.setItem("activeEnlaceId", enlaceId);

        // Cambiamos el color de fondo del enlace clicado a amarillo (#FFFF00)
        enlace.style.backgroundColor = '#C88E0B80';

        // Restablecemos el color de fondo a blanco para las otras etiquetas
        enlaces.forEach(enlaceIterado => {
            if (enlaceIterado !== enlace) {
                enlaceIterado.style.backgroundColor = '#ffffff';
            }
        });
    });
});

// Agregamos un evento de clic al enlace fuera del contenedor para borrar el valor del localStorage
const enlaceBorrarLocalStorage = document.getElementById("tienda");
enlaceBorrarLocalStorage.addEventListener("click", () => {
    localStorage.removeItem("activeEnlaceId");

    // Restablecemos el color de fondo a blanco para todas las etiquetas dentro del contenedor
    enlaces.forEach(enlace => {
        enlace.style.backgroundColor = '#ffffff';
    });
});

// Verificamos si hay un id de enlace activo almacenado en localStorage
const activeEnlaceId = localStorage.getItem("activeEnlaceId");

if (activeEnlaceId) {
    // Si hay un id de enlace activo, buscamos el enlace correspondiente y cambiamos su color de fondo a amarillo
    const activeEnlace = document.querySelector(`[data-id="${activeEnlaceId}"]`);
    activeEnlace.style.backgroundColor = '#C88E0B80';
}
