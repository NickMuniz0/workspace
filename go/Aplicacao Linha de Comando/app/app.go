package app

import (
	"net"
	"github.com/urfave/cli"
)


func Gerar() *cli.App {

	app := cli.NewApp()
	app.Name = "Aplicação de Linha de Comando"
	app.Usage = "Uma aplicação exemplo para linha de comando em Go"

	flags := []cli.Flag{
				cli.StringFlag{
					Name:  "host",
					Value: "devbook.com.br",
				},
			}

	app.Commands = []cli.Command{
		{
			Name:    "ip",
			Usage:   "Busca o endereço IP na internet",
			Flags: flags ,
			Action: buscarIP,
		},
		{
			Name:    "servidores",
			Usage:   "Busca o nome do servidor na internet",
			Flags: flags ,
			Action: buscarServidor,
		},

	}

	return app

}
func buscarServidor(c *cli.Context) error {
	host := c.String("host")
	println("Buscando o nome do servidor do host:", host)
	servidores, erro := net.LookupNS(host)
	if erro != nil {
		println("Erro ao buscar o nome do servidor:", erro.Error())
		return erro
	}
	for _, servidor := range servidores {
		println("Servidor encontrado:", servidor.Host)
	}	
	return nil
}

func buscarIP(c *cli.Context) error {
	host := c.String("host")
	println("Buscando o IP do host:", host)
	ips, erro := net.LookupIP(host)
	if erro != nil {
		println("Erro ao buscar o IP:", erro.Error())
		return erro
	}
	for _, ip := range ips {
		println("IP encontrado:", ip.String())
	}
	return nil

}

