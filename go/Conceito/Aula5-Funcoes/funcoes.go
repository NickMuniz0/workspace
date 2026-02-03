package main

func somar(a int, b int) int {
	return a + b
}

// Função com múltiplos retornos
// Soma, Subtracao, Multiplicacao e Divisao
func calculosMatematicos(a int, b int) (int, int, int, float64) {
	soma := a + b
	subtracao := a - b
	multiplicacao := a * b
	divisao := float64(a) / float64(b)
	return soma, subtracao, multiplicacao, divisao
}

func main() {
	soma := somar(10, 20)
	println(soma)

	// Função Anônima
	var f = func(txt string) string {
		println(txt)
		return txt

	}
	// Chamando a função anônima
	resultado := f("Função Anônima")
	print(resultado)

	// Chamando a função com múltiplos retornos
	a, b, c, d := calculosMatematicos(10, 20)
	println("Soma:", a)
	println("Subtração:", b)
	println("Multiplicação:", c)
	println("Divisão:", d)

	// Ignorando valores indesejados
	a, _, _, _ = calculosMatematicos(30, 15)
	println("Soma:", a)
}
