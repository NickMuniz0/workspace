package main

func soma(numeros ...int) int {
	println(numeros)
	total := 0
	for _, numero := range numeros {
		total += numero
	}
	return total
}

// Nãop pode ter mais de um parametro variatico e obrigatoriamente deve ser o ultimo
func escrever(texto string, numeros ...int) {
	for _, numero := range numeros {
		println(texto, numero)
	}

}
func main() {
	resultado := soma(1, 2, 3, 4, 5)
	println("Soma:", resultado)
	escrever("Número:", 10, 20, 30, 40, 50)

}
