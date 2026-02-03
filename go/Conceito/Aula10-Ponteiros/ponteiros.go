package main

func main() {

	var variavel1 int = 10
	println("Valor da variável:", variavel1)
	var variavel2 int = variavel1
	println("Valor da variável 2:", variavel2)
	variavel2++
	println("Valor da variável 2 após incremento:", variavel2)

	var variavel3 int
	var ponteiro *int
	println(variavel3, ponteiro)

	variavel3 = 100
	ponteiro = &variavel3
	println("Valor da variável 3:", variavel3)
	println("Endereço da variável 3:", &variavel3)
	println("Valor do ponteiro:", ponteiro)
	println("Valor apontado pelo ponteiro:", *ponteiro)
}
