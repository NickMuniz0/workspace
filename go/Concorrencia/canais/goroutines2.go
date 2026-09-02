package main

import "time"

func main() {
	canal := make(chan string)
	go escrever2("Olá Mundo", canal)
	for mensagem := range canal {
		println(mensagem)
	}

}
func escrever2(texto string, canal chan string) {
	for i := 0; i < 5; i++ {
		canal <- texto
		time.Sleep(time.Second)

	}
	close(canal)
}
