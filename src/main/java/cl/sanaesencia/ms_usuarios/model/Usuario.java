package cl.sanaesencia.ms_usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad que se guarda en la tabla "usuarios" de MySQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    // Clave primaria autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // No se pueden repetir correos
    @Column(nullable = false, unique = true)
    private String correo;

    // Se guarda cifrada, nunca en texto plano
    @Column(nullable = false)
    private String clave;

    private String telefono;

    // Se guarda como texto ("PACIENTE", "TERAPEUTA", "ADMIN")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Solo para terapeutas: id del profesional en el catálogo (0 si no aplica)
    private Integer terapeutaId = 0;
}