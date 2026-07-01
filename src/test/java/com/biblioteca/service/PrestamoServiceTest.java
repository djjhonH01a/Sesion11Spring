package com.biblioteca.service;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.exception.LibroNoDisponibleException;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    @Test
    void registrarPrestamo_exitoso() {
        Usuario usuario = new Usuario("Ana", "ana@mail.com");
        usuario.setId(1L);

        Libro libro = new Libro("El Quijote", "Cervantes", true);
        libro.setId(1L);

        PrestamoRequest request = new PrestamoRequest(1L, 1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        Prestamo resultado = prestamoService.registrarPrestamo(request);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(libro, resultado.getLibro());
        assertNotNull(resultado.getFechaPrestamo());

        ArgumentCaptor<Libro> libroCaptor = ArgumentCaptor.forClass(Libro.class);
        verify(libroRepository).save(libroCaptor.capture());
        assertFalse(libroCaptor.getValue().isDisponible());
    }

    @Test
    void registrarPrestamo_libroNoDisponible() {
        Usuario usuario = new Usuario("Ana", "ana@mail.com");
        usuario.setId(1L);

        Libro libro = new Libro("Cien años de soledad", "García Márquez", false);
        libro.setId(2L);

        PrestamoRequest request = new PrestamoRequest(2L, 1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(libroRepository.findById(2L)).thenReturn(Optional.of(libro));

        assertThrows(LibroNoDisponibleException.class,
                () -> prestamoService.registrarPrestamo(request));
    }
}
