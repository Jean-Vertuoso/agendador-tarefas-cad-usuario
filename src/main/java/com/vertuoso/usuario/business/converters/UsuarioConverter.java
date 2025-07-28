package com.vertuoso.usuario.business.converters;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vertuoso.usuario.business.dto.EnderecoDTO;
import com.vertuoso.usuario.business.dto.TelefoneDTO;
import com.vertuoso.usuario.business.dto.UsuarioDTO;
import com.vertuoso.usuario.infrastructure.entities.Endereco;
import com.vertuoso.usuario.infrastructure.entities.Telefone;
import com.vertuoso.usuario.infrastructure.entities.Usuario;

@Component
public class UsuarioConverter {

    // CONVERTER DE USUARIODTO PARA USUARIO
    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setEnderecos(paraListaEndereco(usuarioDTO.getEnderecos()));
        usuario.setTelefones(paraListaTelefone(usuarioDTO.getTelefones()));

        return usuario;
    }

    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS){
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        Endereco endereco = new Endereco();
        endereco.setRua(enderecoDTO.getRua());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setEstado(enderecoDTO.getEstado());
        return endereco;
    }

    public List<Telefone> paraListaTelefone(List<TelefoneDTO> telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        Telefone telefone = new Telefone();
        telefone.setDdd(telefoneDTO.getDdd());
        telefone.setNumero(telefoneDTO.getNumero());
        return telefone;
    }

    // CONVERTER DE USUARIO PARA USUARIODTO
    public UsuarioDTO paraUsuarioDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getUsername());
        usuarioDTO.setSenha(usuario.getPassword());
        usuarioDTO.setEnderecos(paraListaEnderecoDTO(usuario.getEnderecos()));
        usuarioDTO.setTelefones(paraListaTelefoneDTO(usuario.getTelefones()));
        return usuarioDTO;
    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> endereco){
        return endereco.stream().map(this::paraEnderecoDTO).toList();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco){
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(endereco.getId());
        enderecoDTO.setRua(endereco.getRua());
        enderecoDTO.setNumero(endereco.getNumero());
        enderecoDTO.setCidade(endereco.getCidade());
        enderecoDTO.setComplemento(endereco.getComplemento());
        enderecoDTO.setCep(endereco.getCep());
        enderecoDTO.setEstado(endereco.getEstado());
        return enderecoDTO;
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefone){
        return telefone.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone){
        TelefoneDTO telefoneDTO = new TelefoneDTO();
        telefoneDTO.setId(telefone.getId());
        telefoneDTO.setDdd(telefone.getDdd());
        telefoneDTO.setNumero(telefone.getNumero());
        return telefoneDTO;
    }

    // ATUALIZAR DADOS DO USUARIO RECEBENDO DADOS DO FRONT. CASO NÃO TENHA, PEGA DO BANCO DE DADOS.
    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity){
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome());
        usuario.setId(entity.getId());
        usuario.setSenha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : entity.getPassword());
        usuario.setEmail(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : entity.getUsername());
        usuario.setEnderecos(entity.getEnderecos());
        usuario.setTelefones(entity.getTelefones());

        return usuario;
    }

    public Endereco updateEndereco(EnderecoDTO dto, Endereco entity){
        Endereco endereco = new Endereco();
        endereco.setId(entity.getId());
        endereco.setRua(dto.getRua() != null ? dto.getRua() : entity.getRua());
        endereco.setNumero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero());
        endereco.setCidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade());
        endereco.setCep(dto.getCep() != null ? dto.getCep() : entity.getCep());
        endereco.setComplemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento());
        endereco.setEstado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado());
        endereco.setUsuarioId(entity.getUsuarioId());

        return endereco;
    }

    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity){
        Telefone telefone = new Telefone();
        telefone.setId(entity.getId());
        telefone.setDdd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd());
        telefone.setNumero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero());
        telefone.setUsuarioId(entity.getUsuarioId());

        return telefone;
    }

    public Endereco paraEnderecoEntity(EnderecoDTO enderecoDTO, Long idUsuario){
        Endereco endereco = new Endereco();
        endereco.setRua(enderecoDTO.getRua());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setEstado(enderecoDTO.getEstado());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setUsuarioId(idUsuario);

        return endereco;
    }
    public Telefone paraTelefoneEntity(TelefoneDTO telefoneDTO, Long idUsuario){
        Telefone telefone = new Telefone();
        telefone.setDdd(telefoneDTO.getDdd());
        telefone.setNumero(telefoneDTO.getNumero());
        telefone.setUsuarioId(idUsuario);

        return telefone;
    }
}
