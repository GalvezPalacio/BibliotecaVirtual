document.addEventListener("DOMContentLoaded", () => {
    const verUsuariosBtn = document.getElementById("verUsuariosBtn");
    const listaUsuarios = document.getElementById("listaUsuarios");
    let visible = false;

    verUsuariosBtn.addEventListener("click", async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            window.location.href = "login.html";
            return;
        }

        if (visible) {
            listaUsuarios.innerHTML = "";
            verUsuariosBtn.textContent = "Ver todos los usuarios";
            visible = false;
            return;
        }

        try {
            const response = await fetch("http://localhost:8082/api/usuarios/usuarios/todos", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

            if (!response.ok)
                throw new Error("Error al obtener los usuarios");

            const usuarios = await response.json();

            listaUsuarios.innerHTML = "";

            usuarios.forEach(usuario => {
                const item = document.createElement("li");
                item.className = "tarjeta-usuario";

                const librosTexto = usuario.libros.length > 0
                        ? usuario.libros.map(l => `📖 ${l}`).join("<br>")
                        : "<em>Sin libros alquilados</em>";

                item.innerHTML = `
                    <h3>${usuario.nombre}</h3>
                    <p><strong>Email:</strong> ${usuario.email}</p>
                    <p><strong>Tipo:</strong> ${usuario.tipo}</p>
                    <p><strong>Libros alquilados:</strong><br>${librosTexto}</p>
                    ${usuario.libros.length === 0
                        ? `<button class="btn-eliminar" onclick="eliminarUsuario('${usuario.email}')">🗑️ Eliminar</button>`
                        : ''}
                `;

                listaUsuarios.appendChild(item);
            });

            verUsuariosBtn.textContent = "Ocultar usuarios";
            visible = true;

        } catch (err) {
            alert("❌ Error al cargar los usuarios: " + err.message);
        }
    });
});

function mostrarFormularioBibliotecario() {
    document.getElementById("formulario-bibliotecario").style.display = "block";
}

function cerrarFormularioBibliotecario() {
    document.getElementById("formulario-bibliotecario").style.display = "none";
}

async function crearBibliotecario() {
    const nombre = document.getElementById("nuevoNombre").value;
    const email = document.getElementById("nuevoEmail").value;
    const contrasena = document.getElementById("nuevaContrasena").value;

    if (!nombre || !email || !contrasena) {
        alert("⚠️ Todos los campos son obligatorios");
        return;
    }

    const token = localStorage.getItem("token");

    try {
        const response = await fetch("http://localhost:8082/api/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                nombre: nombre,
                email: email,
                contrasena: contrasena,
                tipo: "bibliotecario"
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText);
        }

        alert("✅ Bibliotecario creado correctamente");
        cerrarFormularioBibliotecario();
        document.getElementById("verUsuariosBtn").click();

    } catch (err) {
        alert("❌ Error al crear bibliotecario: " + err.message);
    }
}

async function eliminarUsuario(email) {
    const confirmacion = confirm(`¿Seguro que deseas eliminar al usuario ${email}?`);
    if (!confirmacion)
        return;

    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`http://localhost:8082/api/usuarios/${encodeURIComponent(email)}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok)
            throw new Error("Error al eliminar usuario");

        alert("✅ Usuario eliminado correctamente");
        document.getElementById("verUsuariosBtn").click(); // refrescar lista

    } catch (err) {
        alert("❌ No se pudo eliminar: " + err.message);
    }
}
