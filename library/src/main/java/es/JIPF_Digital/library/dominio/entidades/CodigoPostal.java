package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;

// Definición del enum con constantes válidas
public enum CodigoPostal {
    CP_45600,   // Los nombres de constantes deben ser válidos
    CP_28000;   // Punto y coma al final si hay atributos o métodos adicionales

    // Atributo del enum
    Collection<Repartidor> repartidores;
}