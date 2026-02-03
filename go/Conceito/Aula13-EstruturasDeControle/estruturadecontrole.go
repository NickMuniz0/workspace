package main

func main() {

	numero := 10
	if numero > 15 {
		println("O número é maior que 15")
	} else if numero < 5 {
		println("O número é menor que 5")
	} else {
		println("O número está entre 5 e 15")
	}

	if outronumero := numero; outronumero > 0 {
		println("O número é positivo")
	}
	//if init limita ao escopo no if fora do bloco não é possível acessar outronumero
}
