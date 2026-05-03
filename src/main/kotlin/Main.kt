fun criaMenu(): String {
    return "\nBem vindo ao Campo DEISIado" +
            "\n\n1 - Novo Jogo" +
            "\n2 - Ler Jogo" +
            "\n0 - Sair\n"
}

fun validaNome(nome: String, tamanhoMinimo: Int = 3): Boolean {
    val nomeValidado = nome.trim()
    if (nomeValidado.isEmpty()) return false
    if (nomeValidado.count { it.isLetter() } < tamanhoMinimo) return false

    var testes = 0
    var contadores = 0

    while (testes < nomeValidado.length) {
        val caractere = nomeValidado[testes]
        if (caractere == ' ' && testes + 1 < nomeValidado.length && !nomeValidado[testes + 1].isUpperCase()) {
            return false
        }
        if (caractere == ' ') {
            contadores++
        } else if (!caractere.isLetter()) {
            return false
        }
        testes++
    }
    if (contadores == 0) return false
    return true
}

fun validaNumeroDeMinas(l: Int, c: Int, numminas: Int): Boolean {
    val space = (l * c) - 2
    return numminas in 1..space
}

fun calculaNumeroDeMinas(l: Int, c: Int): Int {
    val space = (l * c) - 2
    return when (space) {
        1 -> 1
        in 2..5 -> 2
        in 6..10 -> 3
        in 11..20 -> 6
        in 21..50 -> 10
        else -> 15
    }
}

fun criaLegenda(c: Int): String {
    var resultado = ""
    var count = 0
    val letraa = 'A'
    while (count < c) {
        val letra = letraa + count
        resultado += "$letra"
        if (count < c - 1) resultado += "   "
        count++
    }
    return resultado
}


fun criaTerreno(matriz: Array<Array<Pair<String, Boolean>>>, mostraLegenda: Boolean, mostraTudo: Boolean): String {

    var terreno = ""

    if (mostraLegenda) {
        terreno += "    " + criaLegenda(matriz[0].size) + "    \n"
    }

    var contadorLinha = 0
    for (linha in matriz) {

        if (mostraLegenda) {
            terreno += " ${contadorLinha + 1} "
        }

        var contadorColuna = 0
        for (celula in linha) {
            val simbolo = celula.first
            val visivel = celula.second

            val aMostrar = if (visivel || mostraTudo) {
                simbolo
            } else " "
            terreno += " $aMostrar "

            if (contadorColuna < linha.size - 1) {
                terreno += "|"
            }

            contadorColuna++
        }

        if (mostraLegenda) {
            terreno += "   "
        }


        if (contadorLinha < matriz.size - 1) {
            terreno += "\n"

            if (mostraLegenda) {
                terreno += "   "
            }

            contadorColuna = 0
            for (celula in linha) {

                terreno += "---"

                if (contadorColuna < linha.size - 1) {
                    terreno += "+"
                }
                contadorColuna++
            }

            if (mostraLegenda) {

                terreno += "   "
            }

            terreno += "\n"
        } else if (mostraLegenda) {
            terreno += "\n"
        }

        contadorLinha++
    }

    return terreno
}


fun obtemCoordenadas(coordenadas: String?): Pair<Int, Int>? {
    if ((coordenadas == null) || (coordenadas.length != 2)) {
        return null
    } else if ((coordenadas[0] !in '1'..'9')) {
        return null
    }
    val num1 = coordenadas[0] - '1'
    val letra1 = coordenadas[1].lowercaseChar()

    val num2 = if ((letra1 in 'a'..'z')) {
        letra1 - 'a'
    } else {
        return null
    }

    return Pair(num1, num2)

}

fun validaMovimentoJogador(parOrigem: Pair<Int, Int>, parDestino: Pair<Int, Int>): Boolean {
    val dest = parDestino
    val orig = parOrigem
    if ((dest.first in (orig.first - 2)..(orig.first + 2)) && (dest.second in (orig.second - 2)..(orig.second + 2))) {
        return true
    } else {
        return false
    }

}

fun quadradoAVoltaDoPonto(
    linha: Int,
    coluna: Int,
    numLinha: Int,
    numColuna: Int
): Pair<Pair<Int, Int>, Pair<Int, Int>> {

    val linhaCima = linha - 1
    val linhaBaixo = linha + 1
    val colunaCima = coluna - 1
    val colunaBaixo = coluna + 1

    return Pair(Pair(linhaCima, colunaCima), Pair(linhaBaixo, colunaBaixo))
}


fun revelaMatriz(
    matriz: Array<Array<Pair<String, Boolean>>>,
    linha: Int,
    coluna: Int
): Array<Array<Pair<String, Boolean>>> {

    if (linha < 0 || linha >= matriz.size || coluna < 0 || coluna >= matriz[0].size) {
        return matriz
    }

    if ((matriz[linha][coluna].first == "*")) {
        return matriz
    }

    matriz[linha][coluna] = Pair(matriz[linha][coluna].first, true)

    val limites = quadradoAVoltaDoPonto(linha, coluna, matriz.size, matriz[0].size)

    val linhaInicial = limites.first.first
    val colunaInicial = limites.first.second

    val linhaFinal = limites.second.first
    val colunaFinal = limites.second.second


    for (linhaAtual in linhaInicial..linhaFinal) {
        for (colunaAtual in colunaInicial..colunaFinal) {

            if ((linhaAtual >= 0) && (linhaAtual < matriz.size) && (colunaAtual >= 0) && (colunaAtual < matriz[0].size)) {

                if (matriz[linhaAtual][colunaAtual].first != "*") {

                    matriz[linhaAtual][colunaAtual] = Pair(matriz[linhaAtual][colunaAtual].first, true)

                }
            }
        }
    }

    return matriz
}


fun celulaTemNumeroMinasVisivel(matriz: Array<Array<Pair<String, Boolean>>>, linha: Int, coluna: Int): Boolean {
    if ((matriz[linha][coluna].first != "*")) {
        if ((matriz[linha][coluna].first in "0".."9")) {

            return matriz[linha][coluna].second

        } else {

            return false
        }
    } else {
        return false
    }
}

fun escondeMatriz(matriz: Array<Array<Pair<String, Boolean>>>): Array<Array<Pair<String, Boolean>>> {

    for (linha in 0..<matriz.size) {
        for (coluna in 0..<matriz[linha].size) {

            if ((matriz[linha][coluna].first != "J") && (matriz[linha][coluna].first != "f")) {

                matriz[linha][coluna] = Pair(matriz[linha][coluna].first, false)

            }
        }
    }
    return matriz

}

fun validaCoordenadasDentroTerreno(coord: Pair<Int, Int>, numLinhas: Int, numColunas: Int): Boolean {
    val linha = coord.first
    val coluna = coord.second

    if (linha < 0 || linha >= numLinhas) {
        return false
    }
    if (coluna < 0 || coluna >= numColunas) {
        return false
    }

    return true
}

fun geraMatrizTerreno(
    linhas: Int,
    colunas: Int,
    numMinas: Int
): Array<Array<Pair<String, Boolean>>> {

    val matriz = Array(linhas) {
        Array(colunas) { Pair(" ", false) }
    }

    matriz[0][0] = Pair("J", true)
    matriz[linhas - 1][colunas - 1] = Pair("f", true)

    var minasColocadas = 0

    while (minasColocadas < numMinas) {

        val linha = (Math.random() * linhas).toInt()
        val coluna = (Math.random() * colunas).toInt()

        if (
            !(linha == 0 && coluna == 0) &&
            !(linha == linhas - 1 && coluna == colunas - 1) &&
            matriz[linha][coluna].first == " "
        ) {
            matriz[linha][coluna] = Pair("*", false)
            minasColocadas++
        }
    }

    return matriz
}

fun preencheNumMinasNoTerreno(matriz: Array<Array<Pair<String, Boolean>>>) {
    for (linha in matriz.indices) {
        for (coluna in matriz[linha].indices) {
            val simbolo = matriz[linha][coluna].first
            if (simbolo != "*" && simbolo != "J" && simbolo != "f") {
                val minas = contaMinasPerto(matriz, linha, coluna)
                if (minas > 0) {
                    matriz[linha][coluna] = Pair(minas.toString(), false)
                }
            }
        }
    }
}

fun contaMinasPerto(matriz: Array<Array<Pair<String, Boolean>>>, linhas: Int, colunas: Int): Int {

    var minas = 0

    for (deltaLinha in -1..1) {
        for (deltaColuna in -1..1) {
            if (deltaLinha != 0 || deltaColuna != 0) {
                val novaLinha = linhas + deltaLinha
                val novaColuna = colunas + deltaColuna

                if (novaLinha in matriz.indices &&
                    novaColuna in matriz[0].indices &&
                    matriz[novaLinha][novaColuna].first == "*"
                ) {
                    minas++
                }
            }
        }
    }

    return minas
}

fun validaTerreno(terreno: Array<Array<Pair<String, Boolean>>>): Boolean {

    if (terreno.isEmpty() || terreno[0].isEmpty()) {
        return false
    }

    val numLinhas = terreno.size
    val numColunas = terreno[0].size

    var contaJ = 0
    var contaF = 0

    for (i in terreno.indices) {

        if (terreno[i].size != numColunas) return false

        for (j in terreno[i].indices) {

            val simbolo = terreno[i][j].first

            when (simbolo) {
                "J" -> {
                    contaJ++
                    if (i != 0 || j != 0) return false
                }

                "f" -> {
                    contaF++
                    if (i != numLinhas - 1 || j != numColunas - 1) return false
                }

                "*", " " -> {}
                in "1".."8" -> {}
                else -> return false
            }
        }
    }

    return contaJ == 1 && contaF == 1
}

fun lerFicheiroJogo(nome: String, numLinhas: Int, numColunas: Int): Array<Array<Pair<String, Boolean>>> {
    val matriz = Array(numLinhas) {
        Array(numColunas) { Pair(" ", false) }
    }
    var validade = true

    val linhas = java.io.File(nome).readLines()

    for (i in 0..<numLinhas) {

        for (j in 0..numColunas - 1) {

            if (i < linhas.size && j < linhas[i].length) {

                var simbolo = linhas[i][j].toString()

                if (simbolo == "J" || simbolo == "f" || simbolo == "." || simbolo in "1".."8" || simbolo == "*") {

                    if (simbolo == ".") {
                        simbolo = " "
                    }
                    val visivel = simbolo == "J" || simbolo == "f"
                    matriz[i][j] = Pair(simbolo, visivel)

                } else {
                    validade = false
                }
            }
        }
    }

    return matriz
}


fun naada() {
    println("Resposta invalida.")
}

fun pedirNome(): String {
    var nome: String
    while (true) {
        println("Introduz o nome do jogador")
        nome = readln()
        if (!validaNome(nome)) {
            naada()
        } else {
            return nome
        }
    }
}

fun escolherLegenda(): Boolean {
    var legenda = ""
    while (legenda != "s" && legenda != "n") {
        println("Mostrar legenda (s/n)?")
        legenda = readln()
        if (!(legenda == "s" || legenda == "n")) {
            naada()
        } else if (legenda == "s") {
            return true
        } else {
            return false
        }
    }
    return false
}

fun pedirLinhas(): Int {
    var linhas = 0
    while (true) {
        println("Quantas linhas?")
        val input = readln()
        linhas = input.toIntOrNull() ?: -1
        if (linhas >= 1) {
            return linhas
        } else {
            naada()
        }
    }
}

fun pedirColunas(): Int {
    val colunas = 0
    while (true) {
        println("Quantas colunas?")
        val colunas = readln().toIntOrNull() ?: -1
        if (colunas in 1..26) {
            return colunas
        } else {
            naada()
        }
    }
}


fun criaTabuleiro(): Array<Array<Pair<String, Boolean>>> {
    val plinhas = pedirLinhas()
    val pcolunas = pedirColunas()

    println("Quantas minas (ou enter para o valor por omissao)?")
    val escminas = readln()
    val minas = if (escminas == "") {
        calculaNumeroDeMinas(plinhas, pcolunas)
    } else {
        val numMinas = escminas.toIntOrNull() ?: -1
        if (validaNumeroDeMinas(plinhas, pcolunas, numMinas)) {
            numMinas
        } else {
            naada()
            calculaNumeroDeMinas(plinhas, pcolunas)
        }

    }

    val tabuleiro = geraMatrizTerreno(plinhas, pcolunas, minas)

    preencheNumMinasNoTerreno(tabuleiro)

    return tabuleiro
}


fun movimentoJogador(tabuleiro: Array<Array<Pair<String, Boolean>>>): Pair<Int, Int>? {

    while (true) {

        println("Introduz a celula destino (ex: 2D)")

        val pedeCoordenadas = readlnOrNull()

        if (pedeCoordenadas == "sair") {

            return Pair(-1, -1)

        } else if (pedeCoordenadas == "abracadabra") {

            println(criaTerreno(tabuleiro, true, true))

        } else {

            val coordenadas = obtemCoordenadas(pedeCoordenadas)

            if (coordenadas == null) {

                return null

            } else {
                return coordenadas
            }

        }
    }

}


fun confirmaMovimento(tabuleiro: Array<Array<Pair<String, Boolean>>>, origem: Pair<Int, Int>): Pair<Int, Int>? {

    var destino = Pair(0, 0)

    val coordsValidas = movimentoJogador(tabuleiro)
    if (coordsValidas == null) {

        return null

    } else {

        if (coordsValidas == Pair(-1, -1)) {

            return Pair(-1, -1)

        }
        if (!(validaCoordenadasDentroTerreno(coordsValidas, tabuleiro.size, tabuleiro[0].size))) {

            return null

        } else if (!validaMovimentoJogador(origem, coordsValidas)) {

            return null

        } else {

            destino = coordsValidas

        }
    }

    return destino
}

fun jogoSemLegenda(tabuleiro: Array<Array<Pair<String, Boolean>>>): Boolean {

    var jogador = Pair(0, 0)

    var gameOn = true

    if (!(validaTerreno(tabuleiro))) {
        gameOn = false

    }

    revelaMatriz(tabuleiro, jogador.first, jogador.second)

    while (gameOn) {

        println(criaTerreno(tabuleiro, false, false))

        val destino = confirmaMovimento(tabuleiro, jogador)

        if (destino == Pair(-1, -1)) {

            return true

        } else if (destino == null) {

            println("Movimento invalido.")

        } else {

            tabuleiro[jogador.first][jogador.second] = Pair(" ", tabuleiro[jogador.first][jogador.second].second)

            val conteudo = tabuleiro[destino.first][destino.second].first

            escondeMatriz(tabuleiro)

            revelaMatriz(tabuleiro, destino.first, destino.second)

            tabuleiro[destino.first][destino.second] = Pair("J", true)

            jogador = destino

            if (conteudo == "*") {
                println("Perdeste o jogo!")
                revelaMatriz(tabuleiro, jogador.first, jogador.second)
                println(criaTerreno(tabuleiro, true, true))
                gameOn = false

            } else if (conteudo == "f") {
                println("ganhaste!")
                revelaMatriz(tabuleiro, jogador.first, jogador.second)
                println(criaTerreno(tabuleiro, true, true))
                gameOn = false
            }
        }
    }
    return false
}

fun jogoComLegenda(tabuleiro: Array<Array<Pair<String, Boolean>>>): Boolean {

    var jogador = Pair(0, 0)

    var gameOn = true

    if (!(validaTerreno(tabuleiro))) {
        gameOn = false

    }

    revelaMatriz(tabuleiro, jogador.first, jogador.second)

    while (gameOn) {

        println(criaTerreno(tabuleiro, true , false))

        val destino = confirmaMovimento(tabuleiro, jogador)

        if (destino == Pair(-1, -1)) {

            return true

        } else if (destino == null) {

            println("Movimento invalido.")

        } else {

            tabuleiro[jogador.first][jogador.second] = Pair(" ", tabuleiro[jogador.first][jogador.second].second)

            val conteudo = tabuleiro[destino.first][destino.second].first

            escondeMatriz(tabuleiro)

            revelaMatriz(tabuleiro, destino.first, destino.second)

            tabuleiro[destino.first][destino.second] = Pair("J", true)

            jogador = destino

            if (conteudo == "*") {
                println("Perdeste o jogo!")
                revelaMatriz(tabuleiro, jogador.first, jogador.second)
                println(criaTerreno(tabuleiro, true, true))
                gameOn = false

            } else if (conteudo == "f") {
                println("ganhaste!")
                revelaMatriz(tabuleiro, jogador.first, jogador.second)
                println(criaTerreno(tabuleiro, true, true))
                gameOn = false
            }
        }
    }
    return false
}


fun vamosLer(): Array<Array<Pair<String, Boolean>>>? {

    var terrenof: Array<Array<Pair<String, Boolean>>>? = null

    var terreno: Array<Array<Pair<String, Boolean>>>

    val plinhas = pedirLinhas()

    val pcolunas = pedirColunas()

    println("Qual o ficheiro de jogo a carregar?")

    val ficheiro = readln()

    terreno = lerFicheiroJogo(ficheiro, plinhas, pcolunas)


    if ((validaTerreno(terreno))) {

        terrenof = terreno
    } else {

        return null
    }

    return terrenof
}


fun main() {

    var count = 0

    while (count == 0) {
        println(criaMenu())
        val menu = readln().toIntOrNull()
        if (menu == null || menu < 0 || menu > 2) {
            naada()
        } else if (menu == 2) {

            val nomeplyr = pedirNome()
            val legendas = escolherLegenda()

            val liTerreno = vamosLer()
            if (liTerreno == null) {
                println("Ficheiro invalido.")
            } else {

                if (legendas) {

                    preencheNumMinasNoTerreno(liTerreno)
                    val lorant = jogoComLegenda(liTerreno)
                    if (lorant == true) {
                        count++
                    }

                } else {

                    preencheNumMinasNoTerreno(liTerreno)
                    val semLegends = jogoSemLegenda(liTerreno)

                    if (semLegends == true) {
                        count++
                    }

                }

            }


        } else if (menu == 0) {
            count++
        } else {

            val nomeplyr = pedirNome()
            val legendas = escolherLegenda()

            if (legendas) {
                val tabuleiroComl = criaTabuleiro()
                val jogoComl = jogoComLegenda(tabuleiroComl)
                if (jogoComl) {
                    count++
                }

            } else {
                val tabuleiroSeml = criaTabuleiro()

                val jogoSeml = jogoSemLegenda(tabuleiroSeml)
                if (jogoSeml) {
                    count++
                }
            }
        }
    }
}