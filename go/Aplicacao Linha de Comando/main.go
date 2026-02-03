package main
//import app
import (
	"linha-de-comando/app"
	"os"
)

func main() {
		println("Aplicação de Linha de Comando")	
		aplicacao := app.Gerar()
		if erro := aplicacao.Run(os.Args); erro != nil {
			println("Erro ao executar a aplicação:", erro.Error())
		}
		
}
