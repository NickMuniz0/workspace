package main

func closure() func() {

	texto := "Dentro da closure"
	funcao := func() {
		println(texto)
	}
	return funcao

}

func main() {

	texto := "Dentro da main"
	println(texto)

	funcaoNova := closure()
	funcaoNova()
}
