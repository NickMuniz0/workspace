package main

func main() {
	// Operadores Aritméticos
	soma := 10 + 5
	subtracao := 10 - 5
	multiplicacao := 10 * 5
	divisao := 10 / 5
	modulo := 10 % 3
	println("Soma:", soma)
	println("Subtração:", subtracao)
	println("Multiplicação:", multiplicacao)
	println("Divisão:", divisao)
	println("Módulo:", modulo)

	// Operadores Relacionais
	igual := 10 == 5
	naoIgual := 10 != 5
	maior := 10 > 5
	menor := 10 < 5
	maiorOuIgual := 10 >= 5
	menorOuIgual := 10 <= 5
	println("Igual:", igual)
	println("Não Igual:", naoIgual)
	println("Maior:", maior)
	println("Menor:", menor)
	println("Maior ou Igual:", maiorOuIgual)
	println("Menor ou Igual:", menorOuIgual)

	// Operadores Lógicos
	and := (10 > 5) && (5 < 3)
	or := (10 > 5) || (5 < 3)
	not := !(10 > 5)
	println("AND:", and)
	println("OR:", or)
	println("NOT:", not)

	// Operadores de Atribuição
	var a int = 10
	a += 5
	println("Atribuição (a += 5):", a)

	// Operadores Unários
	b := 10
	b++
	println("Incremento (b++):", b)
	b--
	println("Decremento (b--):", b)
	b *= -1
	println("Negação (-b):", b)
	b /= -1
	println("Negação novamente (-b):", b)
	b %= 3
	println("Resto da Divisão (b % 3):", b)

	// Operador Ternário (simulado com if-else)
	c := 10
	var resultado string
	if c%2 == 0 {
		resultado = "Par"
	} else {
		resultado = "Ímpar"
	}
	println("Operador Ternário (c é Par ou Ímpar):", resultado)

	// Operadores com Diferentes Tipos de Dados
	var numero1 int16 = 20
	var numero2 int16 = 15
	// var soma2 := numero1 + numero2 nao pode fazer nada com variaveis de tipos diferentes
	var soma2 int16 = numero1 + numero2
	println("Soma de int16:", soma2)

}
