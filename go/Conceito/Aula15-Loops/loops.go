package main

import "time"

func main() {
	i := 10
	for i > 0 {
		println(i)
		i++
		time.Sleep(1 * time.Second)
	}
	println(i)

	for j := 0; j < 10; j++ {
		println(j)
		time.Sleep(1 * time.Second)
	}

	nomes := []string{"Ana", "Bia", "Carlos"}
	for indice, nome := range nomes {
		println(indice, nome)
	}
	//para nao usar o indice, usa-se o _
	doisNomes := []string{"Ana", "Bia", "Carlos"}
	for _, nome := range doisNomes {
		println(nome)

	}

	for indice, letra := range "PALAVRA" {
		println(indice, string(letra))
	}

	usuarios := map[string]string{
		"nome":      "João",
		"sobreNome": "da Silva",
	}
	for chave, valor := range usuarios {
		println(chave, valor)
	}

	//NÂO FUNCIONA COM STRUCTS
	// type usuarioStruct struct {
	// 	nome      string
	// 	sobreNome string
	// }

	// usuario3 := usuarioStruct{
	// 	nome:      "Maria",
	// 	sobreNome: "Oliveira",
	// }
	// for indice, valor := range usuario3 {
	// 	println(indice, valor)
	// }
}
