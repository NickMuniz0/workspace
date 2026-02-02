package main

type usuario struct {
	nome  string
	idade int
}

func (u usuario) salvar() {
	println("Salvando usuário", u.nome, "no banco de dados")
}

func (u usuario) maiorDeIdade() bool {
	return u.idade >= 18
}

func (u *usuario) fazerAniversario() {
	u.idade++
}

func main() {
	usuario1 := usuario{"Ana", 28}
	usuario1.salvar()
	resultado := usuario1.maiorDeIdade()
	println("Ana é maior de idade:", resultado)

	println("Idade de Ana:", usuario1.idade)
	usuario1.fazerAniversario()
	println("Idade de Ana após aniversário:", usuario1.idade)
}
