package main

func main() {

	usuario := map[string]string{
		"nome":      "Nickolas",
		"sobrenome": "Pereira",
		"idade":     "25",
	}
	println("Nome:", usuario["nome"])
	println("Sobrenome:", usuario["sobrenome"])
	println("Idade:", usuario["idade"])

	usuario2 := map[string]map[string]string{
		"nome": {
			"primeiro": "Maria",
		},
		"sobrenome": {
			"ultimo": "Silva",
		},
	}
	println("Nome:", usuario2["nome"]["primeiro"])
	println("Sobrenome:", usuario2["sobrenome"]["ultimo"])

	delete(usuario2, "nome")
	println("Nome após delete:", usuario2)

	usuario2["signo"] = map[string]string{
		"zodiaco": "Leão",
	}
	println("Signo:", usuario2["signo"]["zodiaco"])

}
