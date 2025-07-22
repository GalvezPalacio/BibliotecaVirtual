document.getElementById("formularioLogin").addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const contrasena = document.getElementById("contrasena").value;
    const error = document.getElementById("error");

    try {
        const respuesta = await fetch("http://localhost:8082/auth/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email, contrasena})
        });

        if (!respuesta.ok) {
            throw new Error("Email o contraseña incorrectos");
        }

        const datos = await respuesta.json();
// Verifica qué devuelve el backend
        console.log("Respuesta del backend:", datos);
        localStorage.setItem("token", datos.token);
// Redirigir a la página principal
        window.location.href = "inicio.html";
    } catch (err) {
        error.textContent = err.message;
    }
});
