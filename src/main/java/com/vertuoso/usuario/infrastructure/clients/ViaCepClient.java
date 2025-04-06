package com.vertuoso.usuario.infrastructure.clients;

import com.vertuoso.usuario.business.dto.ViaCepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep", url = "${viacep.url}")
public interface ViaCepClient {

    @GetMapping("/ws/{cep}/json")
    ViaCepDTO buscaDadosEndereco(@PathVariable("cep") String cep);
}