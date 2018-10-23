package com.github.thaynarasilvapinto.simuladorbanco


import com.github.thaynarasilvapinto.simuladorbanco.controller.response.ExtratoResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.SaldoResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import com.fasterxml.jackson.databind.ObjectMapper



@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ContaControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var gson: Gson

    @Before
    fun setup() {
        createClient()
        this.gson = Gson()
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
        val extrato = operacaoService.findAllContaOrigem(joaoConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        contaService.delete(joaoConta.id)
    }

    @Test
    @Throws(Exception::class)
    fun `deve retonar a conta buscada`() {
        this.mvc.perform(get("/conta/{id}", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

    }
    @Test
    @Throws(Exception::class)
    fun `não deve retonar uma conta que não existe no banco`() {
        this.mvc.perform(get("/conta/{id}", -1))
                .andExpect(status().isUnprocessableEntity)

    }

    @Test
    @Throws(Exception::class)
    fun `deve retonar o saldo buscado`() {
        val body = gson.toJson(SaldoResponse(joaoConta))
        this.mvc.perform(get("/conta/{id}/saldo", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().string(body))
    }
    @Test
    @Throws(Exception::class)
    fun `não deve retonar o saldo de um cliente que não existe`() {
        this.mvc.perform(get("/conta/{id}/saldo", -1))
                .andExpect(status().isUnprocessableEntity)
    }
    @Test
    @Throws(Exception::class)
    fun `deve retonar o extrato de um cliente`() {

        var operacaoDeposito: Operacao = Operacao(contaOrigem = joaoConta, contaDestino = joaoConta, valorOperacao = 200.00, tipoOperacao = Operacao.TipoOperacao.DEPOSITO)
        var operacaoSaque: Operacao = Operacao(contaOrigem = joaoConta, contaDestino = joaoConta, valorOperacao = 100.00, tipoOperacao = Operacao.TipoOperacao.SAQUE)

        operacaoDeposito = operacaoService.insert(operacaoDeposito)
        operacaoSaque = operacaoService.insert(operacaoSaque)

        this.mvc.perform(get("/conta/{id}/extrato", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    @Throws(Exception::class)
    fun `não deve retonar o extrato de um cliente que não existe`() {
        this.mvc.perform(get("/conta/{id}/extrato", -1))
                .andExpect(status().isUnprocessableEntity)
    }
}