package cibertec.org.Consultorio_Psicologia.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cibertec.org.Consultorio_Psicologia.entity.Cita;
import cibertec.org.Consultorio_Psicologia.entity.EstadoCita;
import cibertec.org.Consultorio_Psicologia.entity.HorarioDisponible;
import cibertec.org.Consultorio_Psicologia.entity.Psicologo;
import cibertec.org.Consultorio_Psicologia.entity.Usuario;
import cibertec.org.Consultorio_Psicologia.service.CitaService;
import cibertec.org.Consultorio_Psicologia.service.EspecialidadService;
import cibertec.org.Consultorio_Psicologia.service.HorarioDisponibleService;
import cibertec.org.Consultorio_Psicologia.service.PsicologoService;
import cibertec.org.Consultorio_Psicologia.service.UsuarioService;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private CitaService citaService;
    @Autowired
    private EspecialidadService especialidadService;
    @Autowired
    private PsicologoService psicologoService;
    @Autowired
    private HorarioDisponibleService horarioService;

    // ===== DASHBOARD PACIENTE =====
    @GetMapping("/dashboard")
    public String dashboardPaciente(Authentication auth, Model model) {
        try {
            String email = auth.getName();
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                model.addAttribute("usuario", usuario);

                try {
                    List<Cita> citasPaciente = citaService.buscarPorPacienteIdConDetalles(usuario.getId());
                    // Filtrar citas canceladas para la vista del paciente
                    List<Cita> citasActivas = citasPaciente.stream()
                        .filter(cita -> cita.getEstado() != EstadoCita.CANCELADA)
                        .collect(Collectors.toList());
                    
                    model.addAttribute("citasPaciente", citasActivas);
                    model.addAttribute("proximasCitas", citasActivas.stream()
                        .filter(cita -> cita.getFecha() != null && (cita.getFecha().isAfter(LocalDate.now()) ||
                                       cita.getFecha().isEqual(LocalDate.now())))
                        .collect(Collectors.toList()));
                    model.addAttribute("totalCitas", citasActivas.size());
                    model.addAttribute("citasPendientes", citasActivas.stream()
                        .filter(cita -> cita.getEstado() == EstadoCita.PENDIENTE)
                        .count());
                    model.addAttribute("citasConfirmadas", citasActivas.stream()
                        .filter(cita -> cita.getEstado() == EstadoCita.CONFIRMADA)
                        .count());
                } catch (Exception e) {
                    // Valores por defecto en caso de error
                    model.addAttribute("citasPaciente", List.of());
                    model.addAttribute("proximasCitas", List.of());
                    model.addAttribute("totalCitas", 0);
                    model.addAttribute("citasPendientes", 0L);
                    model.addAttribute("citasConfirmadas", 0L);
                    System.out.println("Error cargando citas: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Error general en dashboard: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/login?error";
        }

        return "paciente/dashboard";
    }

    // ===== GESTIÓN DE CITAS DEL PACIENTE =====
    @GetMapping("/citas")
    public String misCitas(Authentication auth, Model model) {
        try {
            String email = auth.getName();
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                model.addAttribute("usuario", usuario);
                
                try {
                    List<Cita> citasPaciente = citaService.buscarPorPacienteIdConDetalles(usuario.getId());
                    // Filtrar citas canceladas para la vista del paciente
                    List<Cita> citasActivas = citasPaciente.stream()
                        .filter(cita -> cita.getEstado() != EstadoCita.CANCELADA)
                        .collect(Collectors.toList());
                    model.addAttribute("citas", citasActivas);
                } catch (Exception e) {
                    // Valores por defecto en caso de error
                    model.addAttribute("citas", List.of());
                    System.out.println("Error cargando citas en mis-citas: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Error general en mis-citas: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/login?error";
        }
        
        return "paciente/mis-citas";
    }

    // ===== AGENDAR NUEVA CITA =====
    @GetMapping("/agendar")
    public String agendarCita(Authentication auth, Model model) {
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);
            model.addAttribute("cita", new Cita());
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("psicologos", psicologoService.listarTodos());
            
            // Horarios disponibles (solo los que están disponibles y son futuras)
            List<HorarioDisponible> horariosDisponibles = horarioService.listarTodos().stream()
                .filter(horario -> horario.getDisponible() && 
                                  horario.getFecha().isAfter(LocalDate.now().minusDays(1)))
                .collect(Collectors.toList());
            model.addAttribute("horariosDisponibles", horariosDisponibles);
        }
        
        return "paciente/agendar-cita";
    }

    @PostMapping("/agendar")
    public String procesarAgendarCita(@ModelAttribute Cita cita, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            String email = auth.getName();
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // Validar que el psicólogo existe
                if (cita.getPsicologo() != null && cita.getPsicologo().getId() != null) {
                    Optional<Psicologo> psicologoOpt = psicologoService.buscarPorId(cita.getPsicologo().getId());
                    if (psicologoOpt.isPresent()) {
                        // Validar que la fecha y hora no sean nulas
                        if (cita.getFecha() == null || cita.getHoraInicio() == null || cita.getMotivo() == null || cita.getMotivo().isBlank()) {
                            redirectAttributes.addFlashAttribute("errorMessage", "Todos los campos de la cita son obligatorios.");
                            return "redirect:/paciente/agendar";
                        }
                        // Crear una nueva cita limpia para evitar problemas de referencia
                        Cita nuevaCita = new Cita();
                        nuevaCita.setPaciente(usuario);
                        nuevaCita.setPsicologo(psicologoOpt.get());
                        nuevaCita.setFecha(cita.getFecha());
                        nuevaCita.setHoraInicio(cita.getHoraInicio());
                        nuevaCita.setMotivo(cita.getMotivo());
                        nuevaCita.setEstado(EstadoCita.PENDIENTE);

                        citaService.guardar(nuevaCita);
                        redirectAttributes.addFlashAttribute("successMessage", "Cita agendada correctamente.");
                    } else {
                        redirectAttributes.addFlashAttribute("errorMessage", "El psicólogo seleccionado no existe.");
                        return "redirect:/paciente/agendar";
                    }
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Debe seleccionar un psicólogo válido.");
                    return "redirect:/paciente/agendar";
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado.");
                return "redirect:/login?error";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al agendar cita: " + e.getMessage());
            return "redirect:/paciente/agendar";
        }

        return "redirect:/paciente/citas";
    }

    // ===== CANCELAR CITA =====
    @GetMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            String email = auth.getName();
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
            Optional<Cita> citaOpt = citaService.buscarPorId(id);
            
            if (usuarioOpt.isPresent() && citaOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                Cita cita = citaOpt.get();
                
                // Verificar que la cita pertenece al paciente logueado
                if (cita.getPaciente().getId().equals(usuario.getId())) {
                    cita.setEstado(EstadoCita.CANCELADA);
                    citaService.guardar(cita);
                    redirectAttributes.addFlashAttribute("successMessage", "Cita cancelada exitosamente");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "No tienes permisos para cancelar esta cita");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cancelar cita: " + e.getMessage());
        }
        
        return "redirect:/paciente/citas";
    }

    // ===== VER PSICÓLOGOS Y ESPECIALIDADES =====
    @GetMapping("/psicologos")
    public String verPsicologos(Authentication auth, Model model) {
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);
            model.addAttribute("psicologos", psicologoService.listarTodos());
            model.addAttribute("especialidades", especialidadService.listarTodos());
        }
        
        return "paciente/psicologos";
    }

    // ===== PERFIL DEL PACIENTE =====
    @GetMapping("/perfil")
    public String verPerfil(Authentication auth, Model model) {
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);
        }
        
        return "paciente/perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute Usuario usuario, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            String email = auth.getName();
            Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(email);
            
            if (usuarioExistente.isPresent()) {
                Usuario usuarioActual = usuarioExistente.get();
                usuarioActual.setNombre(usuario.getNombre());
                usuarioActual.setApellido(usuario.getApellido());
                usuarioActual.setTelefono(usuario.getTelefono());
                
                usuarioService.guardar(usuarioActual);
                redirectAttributes.addFlashAttribute("successMessage", "Perfil actualizado exitosamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar perfil: " + e.getMessage());
        }
        
        return "redirect:/paciente/perfil";
    }
}
