package com.github.thaynarasilvapinto.web.utils

import com.github.thaynarasilvapinto.api.response.*
import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.Operacao

fun Cliente.toResponse(): ClienteResponse =
    ClienteResponse(
        id = this.id,
        cpf = this.cpf,
        nome = this.nome,
        dataHora = this.dataHora,
        conta = this.conta.id
    )

fun Conta.toResponse(): ContaResponse =
    ContaResponse(
        id = this.id,
        saldo = this.saldo,
        dataHora = this.dataHora
    )

fun Operacao.toResponseDeposito(): DepositoResponse =
    DepositoResponse(
        idOperacao = this.idOperacao,
        valorOperacao = this.valorOperacao,
        dataHora = this.dataHoraOperacao,
        tipoOperacao = enumValueOf(this.tipoOperacao.name)
    )

fun Operacao.toResponseSaque(): SaqueResponse =
    SaqueResponse(
        idOperacao = this.idOperacao,
        valorOperacao = this.valorOperacao,
        dataHora = this.dataHoraOperacao,
        tipoOperacao = enumValueOf(this.tipoOperacao.name)
    )

fun Operacao.toResponseTransferencia(): TransferenciaResponse =
    TransferenciaResponse(
        idOperacao = this.idOperacao,
        valorOperacao = this.valorOperacao,
        dataHora = this.dataHoraOperacao,
        tipoOperacao = enumValueOf(this.tipoOperacao.name),
        contaDestino = this.contaDestino.id
    )

fun Conta.toResponseSaldo(): SaldoResponse =
    SaldoResponse(
        saldo = this.saldo
    )