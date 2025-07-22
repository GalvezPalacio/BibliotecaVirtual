document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    try {
        const userResponse = await fetch("http://localhost:8082/api/usuarios/yo", {
            headers: {"Authorization": "Bearer " + token}
        });
        if (!userResponse.ok)
            throw new Error("No se pudo obtener el usuario");

        const usuario = await userResponse.json();
        document.getElementById("mensaje-usuario").textContent = `¡Hola, ${usuario.nombre}! ¿Qué deseas hacer?`;
        localStorage.setItem("nombreUsuario", usuario.nombre);

        const rolResponse = await fetch("http://localhost:8082/api/usuarios/rol", {
            headers: {"Authorization": "Bearer " + token}
        });
        if (!rolResponse.ok)
            throw new Error("No se pudo obtener el rol del usuario");

        const rolData = await rolResponse.json();
        const rol = rolData.rol.toLowerCase();

        console.log("Rol detectado:", rol);
        document.body.style.backgroundImage = rol === "usuario"
                ? "url('img/imagen_fondo_usuario.png')"
                : "url('img/imagen_fondo_bibliotecario.png')";

        const bienvenida = document.getElementById("bienvenida");
        const menuUsuario = document.getElementById("menu-usuario");
        const menuBibliotecario = document.getElementById("menu-bibliotecario");
        const seccionUsuario = document.getElementById("seccion-usuario");

        if (rol === "usuario") {
            bienvenida.textContent = "Bienvenido a tu biblioteca";
            menuUsuario.style.display = "block";
            menuBibliotecario.style.display = "none";
            seccionUsuario.style.display = "none";
        } else {
            bienvenida.textContent = "Panel del bibliotecario";
            menuUsuario.style.display = "none";
            seccionUsuario.style.display = "none";
            menuBibliotecario.style.display = "block";
        }

    } catch (err) {
        alert("Error: " + err.message);
    }
});

function formatearFecha(fechaIso) {
    if (!fechaIso)
        return "";
    const fecha = new Date(fechaIso);
    const dia = String(fecha.getDate()).padStart(2, '0');
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const anio = fecha.getFullYear();
    return `${dia}-${mes}-${anio}`;
}

function mostrarAlquileres() {
    const seccion = document.getElementById('seccion-usuario');
    seccion.style.display = (seccion.style.display === 'none') ? 'block' : 'none';
}

function mostrarFormularioLibro() {
    const formulario = document.getElementById("formulario-anadir-libro");
    formulario.style.display = formulario.style.display === "none" ? "block" : "none";
}

function cerrarSesion() {
    localStorage.removeItem("token");
    localStorage.removeItem("nombreUsuario");
    window.location.href = "login.html";
}

async function devolverLibro(idAlquiler) {
    const token = localStorage.getItem("token");
    try {
        const response = await fetch(`http://localhost:8082/api/alquileres/${idAlquiler}/devolver`, {
            method: "PUT",
            headers: {"Authorization": "Bearer " + token}
        });
        if (!response.ok)
            throw new Error("No se pudo devolver el libro");
        alert("Libro devuelto correctamente");
        location.reload();
    } catch (err) {
        alert("Error: " + err.message);
    }
}

async function añadirLibro() {
    const token = localStorage.getItem("token");

    const titulo = document.getElementById("titulo").value;
    const autorId = document.getElementById("autorId").value;
    const generoId = document.getElementById("generoId").value;
    const anio = document.getElementById("anio").value;

    if (!titulo || !autorId || !generoId || !anio) {
        alert("Por favor, completa todos los campos.");
        return;
    }

    const libro = {
        titulo: titulo,
        anio: parseInt(anio),
        autorId: parseInt(autorId),
        generoId: parseInt(generoId)
    };

    try {
        const response = await fetch("http://localhost:8082/api/libros", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(libro)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error("No se pudo añadir el libro. Detalles: " + errorText);
        }

        alert("Libro añadido correctamente");

        document.getElementById("titulo").value = "";
        document.getElementById("autorId").value = "";
        document.getElementById("generoId").value = "";
        document.getElementById("anio").value = "";

        document.getElementById("formulario-anadir-libro").style.display = "none";

    } catch (err) {
        alert("Error: " + err.message);
    }
}

async function cargarAlquileres() {
    const token = localStorage.getItem("token");
    try {
        const respuesta = await fetch("http://localhost:8082/api/alquileres/mios", {
            headers: {"Authorization": "Bearer " + token}
        });

        if (!respuesta.ok)
            throw new Error("No se pudieron cargar los datos");

        const alquileres = await respuesta.json();
        const lista = document.getElementById("lista-alquileres");
        lista.innerHTML = "";

        alquileres.forEach(alquiler => {
            const li = document.createElement("li");
            const titulo = alquiler.libro.titulo;
            const fechaAlquiler = formatearFecha(alquiler.fechaAlquiler);
            const fechaDevolucion = alquiler.fechaDevolucion
                    ? formatearFecha(alquiler.fechaDevolucion)
                    : null;

            let texto = `${titulo} - <span class="alquilado">Alquilado</span> el ${fechaAlquiler}`;
            li.innerHTML = texto;

            if (!fechaDevolucion) {
                const boton = document.createElement("button");
                boton.textContent = "Devolver";
                boton.classList.add("boton-devolver");
                boton.onclick = async () => {
                    await devolverLibro(alquiler.id);
                };
                li.appendChild(boton);
            } else {
                li.innerHTML += ` - <span class="devuelto">Devuelto</span> el ${fechaDevolucion}`;
            }

            lista.appendChild(li);
        });
    } catch (err) {
        alert("Error al cargar alquileres: " + err.message);
    }
}

// 🆕 FUNCIONALIDAD AÑADIR AUTOR

function mostrarFormularioAutor() {
    document.getElementById("popup-autor").style.display = "block";
}

function cerrarPopupAutor() {
    document.getElementById("popup-autor").style.display = "none";
    document.getElementById("nombreAutor").value = "";
}

async function añadirAutor() {
    const token = localStorage.getItem("token");
    const nombre = document.getElementById("nombreAutor").value.trim();

    if (!nombre) {
        alert("Por favor, introduce un nombre para el autor.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8082/api/autores", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({nombre: nombre})
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error("No se pudo añadir el autor. " + errorText);
        }

        alert("Autor añadido correctamente.");
        cerrarPopupAutor();
    } catch (err) {
        alert("Error: " + err.message);
    }
}

// 🆕 FUNCIONALIDAD AÑADIR GÉNERO

function mostrarFormularioGenero() {
    document.getElementById("popup-genero").style.display = "block";
}

function cerrarPopupGenero() {
    document.getElementById("popup-genero").style.display = "none";
    document.getElementById("nombreGenero").value = "";
}

async function añadirGenero() {
    const token = localStorage.getItem("token");
    const nombre = document.getElementById("nombreGenero").value.trim();

    if (!nombre) {
        alert("Por favor, introduce un nombre para el género.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8082/api/generos", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({nombre: nombre})
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error("No se pudo añadir el género. " + errorText);
        }

        alert("Género añadido correctamente.");
        cerrarPopupGenero();
    } catch (err) {
        alert("Error: " + err.message);
    }
}