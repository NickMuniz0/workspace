package main

type Usuario struct {
	nome  string
	idade int
}

func main() {
	var u Usuario
	u.nome = "João"
	u.idade = 30
	println("Nome:", u.nome)
	println("Idade:", u.idade)

	usuario2 := Usuario{nome: "Maria", idade: 25}
	println("Nome:", usuario2.nome)
	println("Idade:", usuario2.idade)

	usuario3 := Usuario{idade: 40}
	println("Nome:", usuario3.nome)
	println("Idade:", usuario3.idade)

}
