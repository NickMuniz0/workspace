package main

func recuperarExecucao() {
	if r := recover(); r != nil {
		println("Execução recuperada com sucesso:", r)
	}
}

func alunoEstaAprovado(n1, nota float64) bool {
	defer recuperarExecucao()

	media := (n1 + nota) / 2

	if media > 6 {
		return true
	} else if media < 6 {
		return false
	}

	panic("Media igual a 6.0") // Antes de matar a execução ele chama todos os defers

}

func main() {
	println(alunoEstaAprovado(7, 8))
	println(alunoEstaAprovado(6, 6))

	println("Pós Execucao")

}
