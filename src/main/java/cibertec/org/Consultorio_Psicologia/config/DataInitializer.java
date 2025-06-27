package cibertec.org.Consultorio_Psicologia.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cibertec.org.Consultorio_Psicologia.entity.Rol;
import cibertec.org.Consultorio_Psicologia.entity.Usuario;
import cibertec.org.Consultorio_Psicologia.repository.UsuarioRepository;

@Configuration

public class DataInitializer {
    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByemail("admin@consultorio.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setApellido("Principal");
                admin.setEmail("admin@consultorio.com");
                admin.setTelefono("999999999");
                admin.setPassword(passwordEncoder.encode("admin123")); // Encripta aquí
                admin.setRol(Rol.ADMIN);
                admin.setEstado(true);

                usuarioRepository.save(admin);
                System.out.println("✅ Usuario ADMIN creado: admin@consultorio.com / admin123");
            } else {
                System.out.println("ℹ️ Usuario ADMIN ya existe, no se creó uno nuevo.");
            }
        };
    }
}
