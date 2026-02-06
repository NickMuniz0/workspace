package main
import (
	"time"
)

func main(){
	// Concorrencia != Paralelismo

	go escrever("Olá Mundo!")
	escrever("Programando em Go!")

}

func escrever(texto string) {
	for {
		println(texto)
		time.Sleep(1 * time.Second)

	}
}