package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.entidades.*;
import com.example.Cucufato2024.repositorios.RepositorioBusquedas;
import com.example.Cucufato2024.repositorios.RepositorioLineaPrestamo;
import com.example.Cucufato2024.repositorios.RepositorioUsuarios;
import com.example.Cucufato2024.servicios.ServicioBusquedas;
import com.example.Cucufato2024.servicios.ServicioMateriales;
import com.example.Cucufato2024.servicios.ServicioPrestamo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class ControladorDashboard {

    @Autowired
    private ServicioMateriales servicioMateriales;

    @Autowired
    private ServicioPrestamo servicioPrestamo;

    @Autowired
    RepositorioLineaPrestamo repositorioLineaPrestamo;

    @Autowired
    RepositorioUsuarios repositorioUsuarios;

    @Autowired
    private ServicioBusquedas servicioBusquedas;

    private static final Logger logger = LoggerFactory.getLogger(ControladorPrestamos.class);

    @GetMapping
    public String principal(Model model) {
        model.addAttribute("titulo", "Dashboard Principal");
        return "dashboard";
    }

    @GetMapping("/api/informacion")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerInformacionInicial() {
        Map<String, Object> data = Map.of(
            "graficoSeries", servicioMateriales.obtenerDatosGrafico(),
            "prestamosActivos", servicioPrestamo.obtenerPrestamosActivos(),
            "materialesMasPedidos", servicioMateriales.obtenerMaterialesMasPedidos(),
            "busquedas", servicioBusquedas.encontrarBusquedasRecientes(),
            "materiales", servicioMateriales.findAll(),
            "usuarios", repositorioUsuarios.findAll(),
            "indicadores", Map.of(
                "morosidad", servicioPrestamo.calcularTasaMorosidad(),
                "averias", servicioMateriales.calcularTasaAverias(),
                "resolucion", servicioPrestamo.calcularTasaResolucion()
            )
        );
        return ResponseEntity.ok(data);
    }

    @GetMapping("/api/refrescar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> refrescarInformacion() {
        Map<String, Object> dataActualizada = Map.of(
            "graficoSeries", servicioMateriales.obtenerDatosGrafico(),
            "prestamosActivos", servicioPrestamo.obtenerPrestamosActivos(),
            "materialesMasPedidos", servicioMateriales.obtenerMaterialesMasPedidos()
        );
        return ResponseEntity.ok(dataActualizada);
    }

    @GetMapping("/api/prestamos-activos")
    @ResponseBody
    public ResponseEntity<List<Prestamo>> prestamosActivos() {
        List<Prestamo> prestamosActivos = servicioPrestamo.obtenerPrestamosActivos();
        return ResponseEntity.ok(prestamosActivos);
    }

    @GetMapping("/api/materiales-mas-pedidos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> materialesMasPedidos() {
        List<Map<String, Object>> materiales = servicioMateriales.obtenerMaterialesMasPedidos();
        return ResponseEntity.ok(materiales);
    }

    @PostMapping("/crud/busquedas/insertar")
    @ResponseBody
    public ResponseEntity<Boolean> insertarBusqueda(@RequestBody Map<String, Object> data) {
        Busqueda busqueda = new Busqueda();

        // Es bueno cambiar el tipo a Long, como medida preventiva a que el número de búsquedas llegue a rebasar el rango
        busqueda.setEntidadId(Long.valueOf(data.get("id").toString()));
        busqueda.setEntidadType(data.get("tipo").toString());

        servicioBusquedas.save(busqueda);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/log/crear_logs")
    @ResponseBody
    // Aquí se podría validar la sesion del usuario para permitir o denegar la creación de los logs
    // public ResponseEntity<Boolean> insertarLogs(@RequestBody Map<String, Object> data) {
    public ResponseEntity<Boolean> insertarLogs() {
        Map<String, Object> data = Map.of(
                "graficoSeries", servicioMateriales.obtenerDatosGrafico(),
                "prestamosActivos", servicioPrestamo.obtenerPrestamosActivos(),
                "materialesMasPedidos", servicioMateriales.obtenerMaterialesMasPedidos(),
                "busquedas", servicioBusquedas.encontrarBusquedasRecientes(),
                "materiales", servicioMateriales.findAll(),
                "usuarios", repositorioUsuarios.findAll(),
                "indicadores", Map.of(
                        "morosidad", servicioPrestamo.calcularTasaMorosidad(),
                        "averias", servicioMateriales.calcularTasaAverias(),
                        "resolucion", servicioPrestamo.calcularTasaResolucion()
                )
        );
        logger.info("Información del Dashboard: ", data);
        return ResponseEntity.ok(true);
    }
}
