package main

func diaDaSemana(dia int) string {
	switch dia {
	case 1:
		return "Domingo"
	case 2:
		return "Segunda-feira"
	case 3:
		return "Terça-feira"
	case 4:
		return "Quarta-feira"
	case 5:
		return "Quinta-feira"
	case 6:
		return "Sexta-feira"
	case 7:
		return "Sábado"
	default:
		return "Dia inválido"
	}
}

func diaDaSemana2(dia int) string {
	switch {
	case dia == 1:
		return "Domingo"
	case dia == 2:
		return "Segunda-feira"
	case dia == 3:
		return "Terça-feira"
	case dia == 4:
		return "Quarta-feira"
	case dia == 5:
		return "Quinta-feira"
	case dia == 6:
		return "Sexta-feira"
	case dia == 7:
		return "Sábado"
	default:
		return "Dia inválido"
	}
}

var diaDaSemanaVar string

func diaDaSemana3(dia int) string {
	switch {
	case dia == 1:
		diaDaSemanaVar = "Domingo"
		fallthrough // joga para o próximo case
	case dia == 2:
		diaDaSemanaVar = "Segunda-feira"
	case dia == 3:
		diaDaSemanaVar = "Terça-feira"
	case dia == 4:
		diaDaSemanaVar = "Quarta-feira"
	case dia == 5:
		diaDaSemanaVar = "Quinta-feira"
	case dia == 6:
		diaDaSemanaVar = "Sexta-feira"
	case dia == 7:
		diaDaSemanaVar = "Sábado"
	default:
		diaDaSemanaVar = "Dia inválido"

	}
	return diaDaSemanaVar
}

func main() {
	// Exemplo de uso da função diaDaSemana
	println(diaDaSemana(1)) // Output: Domingo
	println(diaDaSemana(5)) // Output: Quinta-feira
	println(diaDaSemana(8)) // Output: Dia inválido
}
