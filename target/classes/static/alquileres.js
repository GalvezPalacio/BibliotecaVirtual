document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch("http://localhost:8082/api/alquileres/todos", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("No se pudieron obtener los alquileres.");
        }

        const alquileres = await response.json();
        const lista = document.getElementById("lista-alquileres-todos");
        lista.innerHTML = "";

        alquileres.forEach(alquiler => {
            const li = document.createElement("li");
            li.textContent = `📘 ${alquiler.libro} · 👤 ${alquiler.usuario}`;
            lista.appendChild(li);
        });

    } catch (err) {
        alert("Error: " + err.message);
    }
});

