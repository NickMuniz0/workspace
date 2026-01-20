package main

import "fmt"

func main() {
	var variavel1 string = "Variavel 1"
	variavel11 := "Variavel 2"
	fmt.Println(variavel1)
	fmt.Println(variavel11)

	var (
		variavel3 string
		variavel4 string
	)

	fmt.Println(variavel3, variavel4)

	variavel5, variavel6 := "Variavel5", "Variavel6"

	fmt.Println(variavel5, variavel6)

	const constante1 string = "Constante 1"
	fmt.Println(constante1)
}
