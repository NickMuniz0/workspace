package main

import (
	json "encoding/json"
	"fmt"
)

type cachorro struct {
	// Nome  string `json:"."` aqui ignora o campo
	Nome  string `json:"nome"`
	Raca  string `json:"raca"`
	Idade int    `json:"idade"`
}

func main() {
	cachorroEmJSON := `{"nome":"Rex","raca":"Labrador","idade":5}`

	var meuCachorro cachorro
	err := json.Unmarshal([]byte(cachorroEmJSON), &meuCachorro)
	if err != nil {
		panic(err)
	}
	fmt.Println(meuCachorro)

	cachorroEmJSON2 := `{"nome":"Bella","raca":"Poodle","idade":"3"}`

	c2 := make(map[string]string)
	if err := json.Unmarshal([]byte(cachorroEmJSON2), &c2); err != nil {
		panic(err)
	}
	fmt.Println(c2)
}
