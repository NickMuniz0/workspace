package main

func inverteSinal(numero int) int {
	return numero * -1
}

func inverteSinalPonteiro(numero *int) {
	*numero = *numero * -1
}
func main() {
	numero := 10
	numeroInvertido := inverteSinal(numero)
	println("Número com sinal invertido:", numeroInvertido)
	println("Número original:", numero)

	numeroInvertidoPonteiro := 20
	println(numeroInvertidoPonteiro)
	inverteSinalPonteiro(&numeroInvertidoPonteiro)
	println("Número com sinal invertido via ponteiro:", numeroInvertidoPonteiro)
}
