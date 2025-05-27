package com.mattfernandes.usuario.business;

import com.mattfernandes.usuario.business.converter.UsuarioConverter;
import com.mattfernandes.usuario.business.dto.UsuarioDTO;
import com.mattfernandes.usuario.infrastructure.entity.Usuario;
import com.mattfernandes.usuario.infrastructure.exception.ConflictException;
import com.mattfernandes.usuario.infrastructure.exception.ResourceNotFoundException;
import com.mattfernandes.usuario.infrastructure.repository.UsuarioRepository;
import com.mattfernandes.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);

            if (existe) {
                throw new ConflictException("E-mail já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("E-mail não encontrado " + email)

        );
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {
        String email = jwtUtil.extractEmailToken(token.substring(7));

        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não localizado.")
        );

        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }

}
