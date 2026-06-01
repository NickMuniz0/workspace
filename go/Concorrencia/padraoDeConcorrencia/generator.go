package main

import (
	"fmt"
	"time"
)

func main() {
  canal := escrever("Olá, mundo!")

  for i := 0; i < 5; i++ {
	fmt.Println(<-canal)
  }
}

//nao precisa criar uma goroutine para ler o canal, pois a função escrever já cria uma goroutine para escrever no canal. A função main pode simplesmente ler do canal usando o operador <-, e isso irá bloquear até que haja um valor disponível para leitura.
func escrever(texto string)<- chan string{
	canal := make(chan string)

	go func(){
		for {
			canal <- fmt.Sprintf("%s", texto)
			time.Sleep(1 * time.Second)
		}
	}()
	return canal
}