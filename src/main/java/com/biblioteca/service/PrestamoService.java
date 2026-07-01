package com.biblioteca.service;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.exception.LibroNoDisponibleException;
import com.biblioteca.exception.LibroNotFoundException;
import com.biblioteca.exception.PrestamoNotFoundException;
import com.biblioteca.exception.UsuarioNotFoundException;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LibroRepository libroRepository,
                           UsuarioRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }

    public Prestamo obtenerPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new PrestamoNotFoundException(id));
    }

    @Transactional
    public Prestamo registrarPrestamo(PrestamoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException(request.getUsuarioId()));

        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new LibroNotFoundException(request.getLibroId()));

        if (!libro.isDisponible()) {
            throw new LibroNoDisponibleException(request.getLibroId());
        }

        libro.setDisponible(false);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDate.now());

        return prestamoRepository.save(prestamo);
    }
}
