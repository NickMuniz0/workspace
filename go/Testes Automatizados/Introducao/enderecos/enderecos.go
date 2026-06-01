package enderecos

import (
	"strings"
)

// TipoDeEndereco verifica se um endereço tem um tipo válido e o retorna
func TipoDeEndereco(endereco string) string {
	tiposValidos := []string{"rua", "avenida", "alameda", "travessa", "praça", "rodovia", "estrada"}
	enderecoEmLetraMinuscula := strings.ToLower(endereco)

	primeiraPalavraDoEndereco := strings.Split(enderecoEmLetraMinuscula, " ")[0]

	enderecoTemUmTipoValido := false

	for _, tipos := range tiposValidos {
		if primeiraPalavraDoEndereco == tipos {
			enderecoTemUmTipoValido = true
			break
		}

	}
	if enderecoTemUmTipoValido {
		return strings.ToTitle(primeiraPalavraDoEndereco)
	}
	return "Tipo Invalido"
}
