package main

func main() {

	retornofunc := func(texto string) string {

		return texto
	}("Função anônima chamada!")

	println(retornofunc)

}
