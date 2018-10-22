package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class ClienteServiceTest(
        @Autowired
        private var clienteService: ClienteService,
        @Autowired
        private var contaService: ContaService,
        private var joao: Cliente,
        private var joaoConta: Conta
) {


    @Before
    fun setup() {
        createClient()
    }

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joaoConta = contaService.insert(joaoConta)
        this.joao = Cliente(nome = "Cliente Test ClienteController", cpf = "151.425.426-75", conta = joaoConta)
        joao = clienteService.insert(joao)
    }

    @After
    fun delete() {
        clienteService.delete(joao.id)
        contaService.delete(joaoConta.id)
    }

    @Test
    fun buscar() {
        val clienteBuscado = clienteService.find(joao.id)
        assertEquals(joao.id.toLong(), clienteBuscado.get().id.toLong())
    }

    @Test
    fun update() {

        joao.nome = "Client Test Update"
        joao = clienteService.update(joao)
        val clienteBuscado = clienteService.find(joao.id)
        assertEquals(joao.nome, clienteBuscado.get().nome)
    }
}
