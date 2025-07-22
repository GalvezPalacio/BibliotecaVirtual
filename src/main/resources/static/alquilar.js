document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch("http://localhost:8082/api/libros/disponibles", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok)
            throw new Error("Error al obtener libros");

        const libros = await response.json();
        const lista = document.getElementById("lista-libros");
        lista.innerHTML = "";

        libros.forEach(libro => {
            const li = document.createElement("li");
            li.innerHTML = `<strong>${libro.titulo}</strong>`;

            const boton = document.createElement("button");
            boton.textContent = "Alquilar";
            boton.classList.add("boton-alquilar");
            boton.onclick = async () => {
                await alquilarLibro(libro.id);
            };

            li.appendChild(boton);
            lista.appendChild(li);
        });

    } catch (err) {
        alert("Error: " + err.message);
    }
});

async function alquilarLibro(idLibro) {
    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`http://localhost:8082/api/alquileres`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({idLibro: idLibro})
        });

        if (!response.ok)
            throw new Error("No se pudo alquilar el libro");

        alert("¡Libro alquilado correctamente!");
        location.reload();

    } catch (err) {
        alert("Error: " + err.message);
    }
}

