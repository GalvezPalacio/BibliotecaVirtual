document.addEventListener("DOMContentLoaded", function () {
    const formulario = document.getElementById("formularioRegistro");
    const error = document.getElementById("error");

    formulario.addEventListener("submit", async function (e) {
        e.preventDefault();

        const nombre = document.getElementById("nombre").value;
        const email = document.getElementById("email").value;
        const contrasena = document.getElementById("contrasena").value;
        const confirmacion = document.getElementById("confirmacion").value;

        if (contrasena !== confirmacion) {
            error.textContent = "Las contraseñas no coinciden";
            return;
        }

        try {
            const respuesta = await fetch("http://localhost:8082/api/usuarios", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    nombre: nombre,
                    email: email,
                    contrasena: contrasena,
                    tipo: "usuario"  // Por defecto, el tipo será 'usuario'
                })
            });

            if (!respuesta.ok) {
                const mensaje = await respuesta.text();
                throw new Error(mensaje || "Error al registrar usuario");
            }

            alert("Usuario registrado con éxito. Ya puedes iniciar sesión.");
            window.location.href = "login.html";

        } catch (err) {
            error.textContent = err.message;
        }
    });
});
