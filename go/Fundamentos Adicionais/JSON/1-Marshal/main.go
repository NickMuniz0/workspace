package main

import (
	"bytes"
	"encoding/json"
	"fmt"
)

type cachorro struct {
	Nome  string `json:"nome"`
	Raca  string `json:"raca"`
	Idade int    `json:"idade"`
}

func main() {
	c := cachorro{
		Nome:  "Rex",
		Raca:  "Labrador",
		Idade: 5,
	}
	cachorroJson, err := json.Marshal(c)
	if err != nil {
		fmt.Println("Erro ao converter para JSON:", err)
		return
	}
	fmt.Println("Cachorro em JSON:", string(cachorroJson))
	fmt.Println(bytes.NewBuffer(cachorroJson))

	c2 := map[string]string{
		"nome": "Rex",
		"raca": "Labrador",
	}
	cachorroJson2, err := json.Marshal(c2)
	if err != nil {
		fmt.Println("Erro ao converter para JSON:", err)
		return
	}
	fmt.Println("Cachorro em JSON (map):", string(cachorroJson2))
}
