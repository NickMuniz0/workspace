package enderecos

import "testing"

//import (
//	. "introducao-testes/enderecos"  // alias para evitar de usar o nome do pacote. usar caso o test esteja  em pacote diferente
//)

// go test --cover
// go test -v -run TestTipoDeEndereco
// go test --coverprofile=coverage.out && go tool cover -html=coverage.out
type cenarioDeTeste struct {
	enderecoInserido string
	retronoEsperado  string
}

// TESTES UNIDADE
func TestTipoDeEndereco(t *testing.T) {
	t.Parallel()
	enderecoParaTeste := "Rua das Flores"
	tipoDeEnderecoEsperado := "RUA"

	tipoDeEnderecoObtido := TipoDeEndereco(enderecoParaTeste)

	if tipoDeEnderecoObtido != tipoDeEnderecoEsperado {
		t.Errorf("Tipo de endereço obtido: %s, Tipo de endereço esperado: %s", tipoDeEnderecoObtido, tipoDeEnderecoEsperado)
	}
}

func TestTipoDeEnderecosCenarios(t *testing.T) {
	t.Parallel()
	cenarioDeTeste := []cenarioDeTeste{
		{enderecoInserido: "Rua das Flores", retronoEsperado: "RUA"},
		{enderecoInserido: "Avenida Paulista", retronoEsperado: "AVENIDA"},
		{enderecoInserido: "Alameda Santos", retronoEsperado: "ALAMEDA"},
		{enderecoInserido: "Travessa do Comércio", retronoEsperado: "TRAVESSA"},
		{enderecoInserido: "Praça da Sé", retronoEsperado: "PRAÇA"},
		{enderecoInserido: "Rodovia dos Imigrantes", retronoEsperado: "RODOVIA"},
		{enderecoInserido: "Estrada do Sol", retronoEsperado: "ESTRADA"},
		{enderecoInserido: "Beco do Batman", retronoEsperado: "Tipo Invalido"},
	}

	for _, cenario := range cenarioDeTeste {
		tipoDeEnderecoObtido := TipoDeEndereco(cenario.enderecoInserido)

		if tipoDeEnderecoObtido != cenario.retronoEsperado {
			t.Errorf("Tipo de endereço obtido: %s, Tipo de endereço esperado: %s", tipoDeEnderecoObtido, cenario.retronoEsperado)
		}
	}
}
