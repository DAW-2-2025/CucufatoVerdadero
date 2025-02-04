const chartCanvas = document.getElementById("chart");
const prestamosTableBody = document.querySelector("#prestamos-activos tbody");
const materialesTableBody = document.querySelector("#materiales-mas-pedidos tbody");
const tasaMorosidadElement = document.querySelector(".tasa-morosidad h1");
const resolucionElement = document.querySelector(".resolucion h1");
const tasaIncidenciasElement = document.querySelector(".tasa-incidencias h1");
const tabsPointer = document.getElementById("tabs-pointer");
const tabs = document.querySelectorAll(".tab");
const searchbarTabsPointer = document.getElementById("searchbar-tabs-pointer");
const searchbarTabs = document.querySelectorAll(".searchbar-tab");
const searchbar = document.getElementById("searchbar");
const resultsList = document.getElementById('results-list');
const recentsList = document.getElementById('recents-list');
const btnCrearLogs = document.getElementById("btn-crear-logs");

let dashboardChart;

function initChart(data) {
    if (!data.rotos && !data.averiados && !data.prestados && !data.disponibles) {
        chartCanvas.parentNode.innerHTML = `
            <div class="flex relative w-full bg-rose-400/25 rounded-lg h-full border border-rose-600/5 text-rose-600">
                <div class="flex items-center gap-3 h-full">
                    <h2 class="text-3xl text-rose-600/50 text-center">No hay datos registrados</h2>
                </div>
                <i class="bi bi-exclamation-triangle absolute -translate-x-1/2 -translate-y-1/2 top-1/2 left-1/2 opacity-10 text-9xl"></i>
            </div>`;
    } else {
        const ctx = chartCanvas.getContext("2d");
        if (dashboardChart) {
            dashboardChart.destroy();
        }

        const dataChart = {
            datasets: [
                {
                    label: "Estado de Materiales",
                    data: [data.funcionales, data.averiados, data.rotos],
                    backgroundColor: ["#4bc0c0", "#ffcd56", "#ff6384"],
                    hoverOffset: 4,
                    customLabels: ["Correctos", "Averiados", "Rotos"],
                },
                {
                    label: "Disponibilidad",
                    data: [data.disponibles, data.prestados],
                    backgroundColor: ["#36a2eb", "#c9cbcf"],
                    hoverOffset: 4,
                    customLabels: ["Disponibles", "Prestados"],
                },
            ],
        };
        const config = {
            type: "doughnut",
            data: dataChart,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        labels: {
                            generateLabels: function(chart) {
                                const original = Chart.overrides.doughnut.plugins.legend.labels.generateLabels;
                                const labelsOriginal = original.call(this, chart);

                                const datasetColors = chart.data.datasets.map(dataset => dataset.backgroundColor).flat();

                                labelsOriginal.forEach(label => {
                                    label.hidden = !chart.isDatasetVisible(label.datasetIndex);
                                    label.fillStyle = datasetColors[label.index];
                                });

                                return labelsOriginal;
                            },
                        },
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                const dataset = context.dataset;
                                const index = context.dataIndex;

                                const customLabel = dataset.customLabels && dataset.customLabels[index]
                                    ? dataset.customLabels[index]
                                    : `Data ${index + 1}`;

                                const value = context.raw;
                                return `${customLabel}: ${value}`;
                            },
                        },
                    },
                },
            },
        };

        dashboardChart = new Chart(ctx, config);
    }
}

function updatePrestamosTable(data) {
    prestamosTableBody.innerHTML = "";
    prestamosTableBody.parentNode.classList.remove('hidden')
    prestamosTableBody.parentNode.nextSibling.nextSibling.nextSibling.nextSibling.classList.add('hidden')
    data.forEach(cabecera_prestamo => {
        cabecera_prestamo.lineas_prestamo.forEach (linea_prestamo => {
            const estaVencido = new Date() - new Date(cabecera_prestamo.fechaDevolucion) < 0;
            const colorEstado = estaVencido ? "emerald" : "rose";
            const row = `
                <tr class="group/row">
                    <td class="py-2 border-b border-gray-200 bg-white text-sm">
                        <div class="ml-3">
                            <p class="text-gray-900 whitespace-no-wrap truncate">
                                ${cabecera_prestamo.usuario}
                            </p>
                            <p class="text-gray-600 whitespace-no-wrap truncate">ID: </p>
                        </div>
                    </td>
                    <td class="py-2 border-b border-gray-200 bg-white text-sm">
                        <div class="flex">
                            <div>
                                <p class="text-gray-900 whitespace-no-wrap truncate">
                                    ${linea_prestamo.material.nombre}
                                </p>
                                <p class="text-gray-600 whitespace-no-wrap truncate">
                                    ${linea_prestamo.cantidad} UDs.
                                </p>
                            </div>
                        </div>
                    </td>
                    <td class="py-2 border-b border-gray-200 bg-white text-sm">
                        <p class="text-${colorEstado}-900 whitespace-no-wrap truncate">${new Date(cabecera_prestamo.fecha).toLocaleDateString()}</p>
                        <p class="text-${colorEstado}-600 whitespace-no-wrap truncate">${ cabecera_prestamo.fechaDevolucion ? new Date(cabecera_prestamo.fechaDevolucion).toLocaleDateString() : "---"}</p>
                    </td>
                    <td class="py-2 border-b border-gray-200 bg-white text-sm">
                        <span class="relative inline-block px-3 py-1 font-semibold text-${colorEstado}-900 leading-tight group-hover/row:hidden">
                            <span aria-hidden
                                class="absolute inset-0 bg-${colorEstado}-200 opacity-50 rounded-full"></span>
                            <span class="relative">${estaVencido ? "Activo" : "Vencido"}</span>
                        </span>
                        <div class="inline-block text-xl text-gray-500 hover:text-gray-700 pe-2">
                            <button class="group/btn">
                                <i class="bi bi-chat-left hidden group-hover/row:inline group-hover/btn:hidden"></i>
                                <a target="_blank" href="https://teams.microsoft.com/l/chat/0/0?users=david.s356811@cesurformacion.com">
                                    <i class="bi bi-chat-left-fill hidden group-hover/btn:inline" title="Abrir chat en Teams"></i>
                                </a> 
                            </button>
    
                            <button class="group/btn">
                                <i class="bi bi-list-task hidden group-hover/row:inline group-hover/btn:hidden"></i>
                                <i class="bi bi-list-check hidden group-hover/btn:inline" title="Ver prestamo"></i>
                            </button>
    
                        </div>
                    </td>
                </tr>
            `;
            prestamosTableBody.innerHTML += row;
        })
    });
}

function updateMaterialesTable(data) {
    materialesTableBody.innerHTML = "";
    materialesTableBody.parentNode.classList.remove('hidden')
    materialesTableBody.parentNode.nextSibling.nextSibling.nextSibling.nextSibling.classList.add('hidden')
    data.forEach (material => {
        const row = `
            <tr class="group/row">
                <td class="py-2 border-b border-gray-200 bg-white text-sm">
                    <div class="ml-3">
                        <p class="text-gray-900 whitespace-no-wrap truncate">
                            ${material.nombre}
                        </p>
                        <p class="text-gray-600 whitespace-no-wrap truncate">ID: ${material.codigoBarras}</p>
                    </div>
                </td>
                <td class="py-2 border-b border-gray-200 bg-white text-sm">
                    <div class="flex">
                        <div>
                            <p class="text-gray-900 whitespace-no-wrap truncate">
                                Total: ${material.total}
                            </p>
                            <p class="text-gray-600 whitespace-no-wrap truncate">
                                ${material.disponibles}
                            </p>
                        </div>
                    </div>
                </td>
                <td class="py-2 border-b border-gray-200 bg-white text-sm">
                    <div class="flex">
                        <div>
                            <p class="text-gray-900 whitespace-no-wrap truncate">
                                ${material.totalPedidos}
                            </p>
                        </div>
                    </div>
                </td>
            </tr>
        `;
        materialesTableBody.innerHTML += row;
    })
}

function updateIndicators(data) {
    tasaMorosidadElement.textContent = `${data.morosidad.toFixed(2)}%`;
    resolucionElement.textContent = `${data.resolucion.toFixed(2)}%`;
    tasaIncidenciasElement.textContent = `${data.averias.toFixed(2)}%`;
}

function initDashboard() {
    fetch("/dashboard/api/informacion")
        .then(response => response.json())
        .then(data => {
            console.log(data);
            initChart(data.graficoSeries);
            updateIndicators(data.indicadores);
            setBusquedasRecientes(data.busquedas);
            refreshSearchbar(data.materiales, data.usuarios, []);
        })
        .catch(err => console.error("Error al obtener datos del gráfico:", err));
}

btnCrearLogs.addEventListener("click", () => {
    fetch("/dashboard/log/crear_logs")
        .then(response => response.json())
        .catch(err => console.error("Error al obtener datos del gráfico:", err));
})

function fetchPrestamosActivos() {
    fetch("/dashboard/api/prestamos-activos")
        .then(response => response.json())
        .then(data => updatePrestamosTable(data))
        .catch(err => console.error("Error al obtener préstamos activos:", err));
}

function fetchMaterialesMasPedidos() {
    fetch("/dashboard/api/materiales-mas-pedidos")
        .then(response => response.json())
        .then(data => updateMaterialesTable(data))
        .catch(err => console.error("Error al obtener materiales más pedidos:", err));
}

function refreshDashboard() {
    initDashboard();
    fetchPrestamosActivos();
    fetchMaterialesMasPedidos();
}

function refreshSearchbar(dataMateriales, dataUsuarios, dataCentros) {
    const dataMaterialesConTipo = dataMateriales.map(item => ({ ...item, tipo: "material" }));
    const dataUsuariosConTipo = dataUsuarios.map(item => ({ ...item, tipo: "usuario" }));
    const dataCentrosConTipo = dataCentros.map(item => ({ ...item, tipo: "Centro" }));

    const data = [...dataMaterialesConTipo, ...dataUsuariosConTipo, ...dataCentrosConTipo];

    const fuse = new Fuse(data, {
        keys: ['nombre'],
        threshold: 0.5,
    });

    const maxRecommendations = 5;

    searchbar.addEventListener('input', () => {
        const query = searchbar.value.trim();
        if (!query) {
            resultsList.innerHTML = '';
            return;
        }

        const results = fuse.search(query).slice(0, maxRecommendations);
        const general = results.map(r => r.item);
        const materials = general.filter(item => item.tipo === 'material');
        const users = general.filter(item => item.tipo === 'user');
        const centros = general.filter(item => item.tipo === 'centro');

        resultsList.innerHTML = "";
        general.map(item => {
            const btn_chat = item.tipo == "usuario"
                ? `<button class="group/btn ms-auto me-3">
                        <i class="bi bi-chat-left hidden group-hover/row:inline group-hover/btn:hidden"></i>
                        <a target="_blank" href="https://teams.microsoft.com/l/chat/0/0?users=${item.correo}">
                            <i class="bi bi-chat-left-fill hidden group-hover/btn:inline" title="Abrir chat en Teams"></i>
                        </a> 
                    </button>`
                : "";

            var color_tipo;
            switch (item.tipo) {
                case "material":
                    color_tipo = "amber";
                    break;
                case "usuario":
                    color_tipo = "sky";
                    break;
                case "centro":
                    color_tipo = "emerald";
                    break;
            }

            resultsList.innerHTML += `<div data-id="${item.id}" data-tipo="${item.tipo}" onclick="resultadoSeleccionado(this)" class="flex w-full ps-3 group/row">
                <div class="w-px bg-slate-600/50 ms-2 me-2"></div>
                <div class="flex items-center w-full gap-x-2 hover:bg-slate-300/90 cursor-pointer p-2 my-0.5 me-2 rounded-md">
                    <img src="https://www.repuestosfuster.com/assets/images/repuestos/fotos/es/default.png" alt="" class="size-8">
                    <div class="bg-${color_tipo}-700/75 p-1 text-xs rounded font-bold text-${color_tipo}-100/50 capitalize">${item.tipo}</div>
                    <span class="">${item.nombre}</span>
                    ${btn_chat}
                </div>
            </div>`;
        })
        .join('');

        // (Opcional) Mostrar resultados en la consola para depuración
        console.log({ general, materials, users, centros });
    });
}

function setBusquedasRecientes(data) {
    data.map(item => {
        var url;
        validarImagen(item.imagen).then(isValid => {
            if (isValid) {
                url = item.imagen;
            } else {
                url = "https://www.repuestosfuster.com/assets/images/repuestos/fotos/es/default.png";
            }

            const btn_chat = item.tipo == "usuario"
                ? `<button class="group/btn ms-auto me-3">
                        <i class="bi bi-chat-left hidden group-hover/row:inline group-hover/btn:hidden"></i>
                        <a target="_blank" href="https://teams.microsoft.com/l/chat/0/0?users=${item.correo}">
                            <i class="bi bi-chat-left-fill hidden group-hover/btn:inline" title="Abrir chat en Teams"></i>
                        </a> 
                    </button>`
                : "";

            var color_tipo;
            switch (item.tipo) {
                case "material":
                    color_tipo = "yellow";
                    break;
                case "usuario":
                    color_tipo = "sky";
                    break;
                case "centro":
                    color_tipo = "emerald";
                    break;
            }

            recentsList.innerHTML += `<div data-id="${item.id}" data-tipo="${item.tipo}" onclick="resultadoSeleccionado(this)" class="flex w-full ps-3 group/row">
                <div class="w-px bg-slate-600/50 ms-2 me-2"></div>
                <div class="flex items-center w-full gap-x-2 hover:bg-slate-300/90 cursor-pointer p-2 my-0.5 me-2 rounded-md">
                    <img src="${url}" alt="" class="size-8">
                    <div class="bg-${color_tipo}-700/75 p-1 text-xs rounded font-bold text-${color_tipo}-100/50 capitalize">${item.tipo}</div>
                    <span class="">${item.nombre}</span>
                    ${btn_chat}
                </div>
            </div>`;
        });
    })
    .join('');
}

async function validarImagen(url) {
    try {
        const response = await fetch(url, { method: 'HEAD' });
        return response.ok && response.headers.get('content-type').startsWith('image/');
    } catch (error) {
        console.error("Error al validar la imagen:", error);
        return false;
    }
}

function resultadoSeleccionado ( e ) {
    const data = {
        id: e.dataset['id'],
        tipo: e.dataset['tipo'],
    }

    fetch("/dashboard/crud/busquedas/insertar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error en la solicitud: ${response.statusText}`);
            }
            return response.json();
        })
        .then(result => {
            console.log("Búsqueda guardada exitosamente:", result);
        })
        .catch(err => {
            console.error("Error al intentar guardar búsqueda:", err);
        });
}

searchbar.addEventListener("focus", e => {
    e.target.parentNode.nextSibling.nextSibling.nextSibling.nextSibling.classList.remove("hidden");
})

searchbar.addEventListener("blur", e => {
    e.target.parentNode.nextSibling.nextSibling.nextSibling.nextSibling.classList.add("hidden");
})

searchbar.addEventListener("input", e => {
    if (e.target.value) {
        e.target.parentNode.nextSibling.nextSibling.classList.remove("hidden");
    } else {
        e.target.parentNode.nextSibling.nextSibling.classList.add("hidden");
    }
})

document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape') {
        searchbar.blur();
        searchbar.value = "";
        searchbar.parentNode.nextSibling.nextSibling.classList.add("hidden");
    } else if (event.ctrlKey && event.key === "b") {
        event.preventDefault();
        const searchbar = document.getElementById("searchbar");
        searchbar.focus();
        const valueLength = searchbar.value.length;
        searchbar.setSelectionRange(valueLength, valueLength);
    }
});

tabs.forEach(el => {
    el.addEventListener("click", e => {
        tabs.forEach(tab => tab.classList.add("text-slate-600/50"));
        tabsPointer.style = `transform: translateX(${ 100 * parseInt(el.dataset['index']) }%)`;
        el.classList.remove("text-slate-600/50");
    })
})

searchbarTabs.forEach(el => {
    el.addEventListener("click", e => {
        searchbarTabs.forEach(tab => tab.classList.add("text-slate-600/50"));
        searchbarTabsPointer.style = `transform: translateX(${ 100 * parseInt(el.dataset['index']) }%)`;
        el.classList.remove("text-slate-600/50");
    })
})

document.addEventListener("DOMContentLoaded", () => {
    refreshDashboard();
    // Opcional, se puede considerar refrescar el dashboard cada 60 segundos
    // Conflicta con la opción de mostrar los informes de usuarios y centros en la misma vista
    // setInterval(refreshDashboard, 60000);
});
