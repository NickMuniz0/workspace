package main

func mensagem1() {
	println("Mensagem 1")
}

func mensagem2() {
	println("Mensagem 2")
}
func mensagem3() {
	println("Mensagem 3")
}

func alunoEstaAprovado(n1, nota float64) bool {
	println("Funcao para verificar se o aluno está aprovado")

	media := (n1 + nota) / 2
	return media >= 7.0
}

func main() {
	defer mensagem1()
	defer mensagem3()
	//Adiar a execução da função mensagem1 até o final da função main
	mensagem2()
	alunoEstaAprovado(7, 8)
}
