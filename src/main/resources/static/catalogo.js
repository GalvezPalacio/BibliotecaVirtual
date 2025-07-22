document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    await cargarCatalogo(token); // 👈 usamos esta función que puedes reutilizar

    async function cargarCatalogo(token) {
        try {
            const response = await fetch("http://localhost:8082/api/libros/catalogo", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

            if (!response.ok)
                throw new Error("No se pudo obtener el catálogo");

            const libros = await response.json();
            const contenedor = document.getElementById("lista-catalogo");
            contenedor.innerHTML = "";

            libros.forEach(libro => {
                const item = document.createElement("li");

                const estadoTexto = libro.disponible
                        ? `<span class="estado-libro disponible">Disponible</span>`
                        : `<span class="estado-libro alquilado">Alquilado por ${libro.nombreUsuario || 'otro usuario'}</span>`;

                const botonEliminar = `<button class="btn-eliminar" onclick="eliminarLibro(${libro.id})">🗑️ Eliminar</button>`;

                item.innerHTML = `
                    <strong>${libro.titulo}</strong>
                    <span>📅 ${libro.anio}</span>
                    ${estadoTexto}
                    ${botonEliminar}
                `;

                contenedor.appendChild(item);
            });

        } catch (err) {
            alert("Error al cargar catálogo: " + err.message);
        }
    }

    window.eliminarLibro = async function (id) {
        const confirmado = confirm("¿Seguro que deseas eliminar este libro?");
        if (!confirmado)
            return;

        try {
            const response = await fetch(`http://localhost:8082/api/libros/${id}`, {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

            if (!response.ok)
                throw new Error("Error al eliminar el libro");

            alert("✅ Libro eliminado correctamente");
            await cargarCatalogo(token);

        } catch (err) {
            alert("❌ No se pudo eliminar: " + err.message);
        }
    };
});