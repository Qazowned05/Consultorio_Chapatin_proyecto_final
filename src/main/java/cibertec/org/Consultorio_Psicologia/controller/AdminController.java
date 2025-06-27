package cibertec.org.Consultorio_Psicologia.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.DocumentException;

import cibertec.org.Consultorio_Psicologia.entity.Cita;
import cibertec.org.Consultorio_Psicologia.entity.Especialidad;
import cibertec.org.Consultorio_Psicologia.entity.EstadoCita;
import cibertec.org.Consultorio_Psicologia.entity.HorarioDisponible;
import cibertec.org.Consultorio_Psicologia.entity.Psicologo;
import cibertec.org.Consultorio_Psicologia.entity.Rol;
import cibertec.org.Consultorio_Psicologia.entity.Usuario;
import cibertec.org.Consultorio_Psicologia.service.CitaService;
import cibertec.org.Consultorio_Psicologia.service.EspecialidadService;
import cibertec.org.Consultorio_Psicologia.service.HorarioDisponibleService;
import cibertec.org.Consultorio_Psicologia.service.PsicologoService;
import cibertec.org.Consultorio_Psicologia.service.ReportService;
import cibertec.org.Consultorio_Psicologia.service.UsuarioService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PsicologoService psicologoService;
    @Autowired
    private CitaService citaService;
    @Autowired
    private EspecialidadService especialidadService;
    @Autowired
    private HorarioDisponibleService horarioService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ===== DASHBOARD =====
    @GetMapping("/panel")
    public String panelAdministrador(Authentication auth, Model model) {
        model.addAttribute("usuario", auth.getName());
        
        // Agregar datos reales al modelo
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalPsicologos", psicologoService.listarTodos().size());
        model.addAttribute("totalEspecialidades", especialidadService.listarTodos().size());
        model.addAttribute("citasRecientes", citaService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("psicologos", psicologoService.listarTodos());
        model.addAttribute("especialidades", especialidadService.listarTodos());
        
        return "admin/panel";
    }

    // ===== GESTIÓN DE USUARIOS =====
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            // Si es un nuevo usuario o se proporciona una nueva contraseña
            if (usuario.getId() == null || (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty())) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                // Mantener la contraseña existente
                Optional<Usuario> usuarioExistente = usuarioService.buscarPorId(usuario.getId());
                if (usuarioExistente.isPresent()) {
                    usuario.setPassword(usuarioExistente.get().getPassword());
                }
            }
            usuario.setEstado(true);
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("roles", Rol.values());
            return "admin/usuario-form";
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    // ===== GESTIÓN DE PSICÓLOGOS =====
    @GetMapping("/psicologos")
    public String listarPsicologos(Model model) {
        model.addAttribute("psicologos", psicologoService.listarTodos());
        return "admin/psicologos";
    }

    @GetMapping("/psicologos/nuevo")
    public String nuevoPsicologo(Model model) {
        model.addAttribute("psicologo", new Psicologo());
        model.addAttribute("especialidades", especialidadService.listarTodos());
        return "admin/psicologo-form";
    }

    @PostMapping("/psicologos/guardar")
    public String guardarPsicologo(@ModelAttribute Psicologo psicologo, RedirectAttributes redirectAttributes) {
        try {
            psicologoService.guardar(psicologo);
            redirectAttributes.addFlashAttribute("successMessage", "Psicólogo guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar psicólogo: " + e.getMessage());
        }
        return "redirect:/admin/psicologos";
    }

    @GetMapping("/psicologos/editar/{id}")
    public String editarPsicologo(@PathVariable Long id, Model model) {
        Optional<Psicologo> psicologo = psicologoService.buscarPorId(id);
        if (psicologo.isPresent()) {
            model.addAttribute("psicologo", psicologo.get());
            model.addAttribute("especialidades", especialidadService.listarTodos());
            return "admin/psicologo-form";
        }
        return "redirect:/admin/psicologos";
    }

    @GetMapping("/psicologos/eliminar/{id}")
    public String eliminarPsicologo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            psicologoService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Psicólogo eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar psicólogo: " + e.getMessage());
        }
        return "redirect:/admin/psicologos";
    }

    // ===== GESTIÓN DE ESPECIALIDADES =====
    @GetMapping("/especialidades")
    public String listarEspecialidades(Model model) {
        model.addAttribute("especialidades", especialidadService.listarTodos());
        return "admin/especialidades";
    }

    @GetMapping("/especialidades/nuevo")
    public String nuevaEspecialidad(Model model) {
        model.addAttribute("especialidad", new Especialidad());
        return "admin/especialidad-form";
    }

    @PostMapping("/especialidades/guardar")
    public String guardarEspecialidad(@ModelAttribute Especialidad especialidad, RedirectAttributes redirectAttributes) {
        try {
            especialidadService.guardar(especialidad);
            redirectAttributes.addFlashAttribute("successMessage", "Especialidad guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar especialidad: " + e.getMessage());
        }
        return "redirect:/admin/especialidades";
    }

    @GetMapping("/especialidades/editar/{id}")
    public String editarEspecialidad(@PathVariable Long id, Model model) {
        Optional<Especialidad> especialidad = especialidadService.buscarPorId(id);
        if (especialidad.isPresent()) {
            model.addAttribute("especialidad", especialidad.get());
            return "admin/especialidad-form";
        }
        return "redirect:/admin/especialidades";
    }

    @GetMapping("/especialidades/eliminar/{id}")
    public String eliminarEspecialidad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            especialidadService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Especialidad eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar especialidad: " + e.getMessage());
        }
        return "redirect:/admin/especialidades";
    }

    // ===== GESTIÓN DE CITAS =====
    @GetMapping("/citas")
    public String listarCitas(Model model) {
        model.addAttribute("citas", citaService.listarTodos());
        return "admin/citas";
    }

    @GetMapping("/citas/nuevo")
    public String nuevaCita(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("psicologos", psicologoService.listarTodos());
        model.addAttribute("estados", EstadoCita.values());
        return "admin/cita-form";
    }

    @PostMapping("/citas/guardar")
    public String guardarCita(@ModelAttribute Cita cita, RedirectAttributes redirectAttributes) {
        try {
            if (cita.getEstado() == null) {
                cita.setEstado(EstadoCita.PENDIENTE);
            }
            citaService.guardar(cita);
            redirectAttributes.addFlashAttribute("successMessage", "Cita guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar cita: " + e.getMessage());
        }
        return "redirect:/admin/citas";
    }

    @GetMapping("/citas/editar/{id}")
    public String editarCita(@PathVariable Long id, Model model) {
        Optional<Cita> cita = citaService.buscarPorId(id);
        if (cita.isPresent()) {
            model.addAttribute("cita", cita.get());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("psicologos", psicologoService.listarTodos());
            model.addAttribute("estados", EstadoCita.values());
            return "admin/cita-form";
        }
        return "redirect:/admin/citas";
    }

    @GetMapping("/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            citaService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cita eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar cita: " + e.getMessage());
        }
        return "redirect:/admin/citas";
    }

    // ===== GESTIÓN DE HORARIOS =====
    @GetMapping("/horarios")
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", horarioService.listarTodos());
        return "admin/horarios";
    }

    @GetMapping("/horarios/nuevo")
    public String nuevoHorario(Model model) {
        model.addAttribute("horario", new HorarioDisponible());
        model.addAttribute("psicologos", psicologoService.listarTodos());
        return "admin/horario-form";
    }

    @PostMapping("/horarios/guardar")
    public String guardarHorario(@ModelAttribute HorarioDisponible horario, RedirectAttributes redirectAttributes) {
        try {
            if (horario.getDisponible() == null) {
                horario.setDisponible(true);
            }
            horarioService.guardar(horario);
            redirectAttributes.addFlashAttribute("successMessage", "Horario guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar horario: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }

    @GetMapping("/horarios/editar/{id}")
    public String editarHorario(@PathVariable Long id, Model model) {
        Optional<HorarioDisponible> horario = horarioService.buscarPorId(id);
        if (horario.isPresent()) {
            model.addAttribute("horario", horario.get());
            model.addAttribute("psicologos", psicologoService.listarTodos());
            return "admin/horario-form";
        }
        return "redirect:/admin/horarios";
    }

    @GetMapping("/horarios/eliminar/{id}")
    public String eliminarHorario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            horarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Horario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar horario: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }
    
    // ===== REPORTES =====
    
    /**
     * Genera y descarga el reporte de citas en PDF
     */
    @GetMapping("/reportes/citas")
    public ResponseEntity<byte[]> generateCitasReportPDF() {
        try {
            byte[] pdfBytes = reportService.generateCitasReport();
            
            String filename = "reporte_citas_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                    ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Genera y descarga el reporte de psicólogos en PDF
     */
    @GetMapping("/reportes/psicologos")
    public ResponseEntity<byte[]> generatePsicologosReportPDF() {
        try {
            byte[] pdfBytes = reportService.generatePsicologosReport();
            
            String filename = "reporte_psicologos_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                    ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generating psychologists report: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}