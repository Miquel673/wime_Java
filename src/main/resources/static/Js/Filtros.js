document.addEventListener("DOMContentLoaded", () => {
      const inputBusqueda = document.getElementById("busqueda");
      const filtroEstado = document.getElementById("filtro-estado");

      if (inputBusqueda) inputBusqueda.addEventListener("input", aplicarFiltros);
      if (filtroEstado) filtroEstado.addEventListener("change", aplicarFiltros);
    });

    function aplicarFiltros() {
      const texto = document.getElementById("busqueda").value.toLowerCase();
      const estadoSeleccionado = document.getElementById("filtro-estado").value;
      const tarjetas = document.querySelectorAll(".card-body");

      tarjetas.forEach(tarjeta => {
        const titulo = tarjeta.querySelector(".card-title")?.textContent.toLowerCase() || "";
        const descripcion = Array.from(tarjeta.querySelectorAll(".card-text"))
                                .map(p => p.textContent.toLowerCase()).join(" ");
        const estado = tarjeta.querySelector(".badge")?.textContent.toLowerCase() || "";

        const coincideTexto = titulo.includes(texto) || descripcion.includes(texto);
        const coincideEstado = !estadoSeleccionado || estado === estadoSeleccionado;

        tarjeta.closest(".col").style.display = (coincideTexto && coincideEstado) ? "block" : "none";
      });
    }

        document.addEventListener("DOMContentLoaded", () => {
      const inputBusqueda = document.getElementById("busqueda");
      if (inputBusqueda) {
        inputBusqueda.addEventListener("input", aplicarFiltroBusqueda);
      }
    });

    function aplicarFiltroBusqueda() {
      const texto = document.getElementById("busqueda").value.toLowerCase();

      const tarjetas = document.querySelectorAll(".card-body");

      tarjetas.forEach(tarjeta => {
        const titulo = tarjeta.querySelector(".card-title")?.textContent.toLowerCase() || "";
        const descripcion = tarjeta.querySelector(".card-text")?.textContent.toLowerCase() || "";
        const coincide = titulo.includes(texto) || descripcion.includes(texto);
        tarjeta.closest(".col").style.display = coincide ? "block" : "none";
      });
    }