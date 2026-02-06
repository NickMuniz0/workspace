package main
import (
	"time"
)

func main(){
	canal := make(chan string)
	go escrever("Olá Mundo", canal)
	for mensagem := range canal {
		println(mensagem)
	}

}
func escrever(texto string,canal chan string) {
	for i := 0; i < 5; i++ {
		canal <- texto
		time.Sleep(time.Second)

	}
	close(canal)
}