package main

type Usuario struct {
	nome  string
	idade int
}

type Pessoa struct {
	nome     string
	idade    int
	endereco string
}

type Estudante struct {
	Pessoa
	curso string
}

func main() {
	p1 := Pessoa{nome: "Carlos", idade: 28, endereco: "Rua A, 123"}
	println("Pessoa:")
	println("Nome:", p1.nome)
	println("Idade:", p1.idade)
	println("Endereço:", p1.endereco)

	e1 := Estudante{p1, "Engenharia"}
	println("Estudante:")
	println("Nome:", e1.nome)
	println("Idade:", e1.idade)
	println("Endereço:", e1.endereco)
	println("Curso:", e1.curso)

}
