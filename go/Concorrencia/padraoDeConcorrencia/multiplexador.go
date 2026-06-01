package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main() {
	canal := multiplexador(write("Olá, mundo!"), write("Go é incrível!"))

	for i := 0; i < 10; i++ {
		fmt.Println(<-canal)
	}
}


func write(texto string)<- chan string{
	canal := make(chan string)

	go func(){
		for {
			canal <- fmt.Sprintf("%s", texto)
			time.Sleep(time.Millisecond * time.Duration(rand.Intn(2000)))
		}
	}()
	return canal
}

func multiplexador(canal1, canal2 <-chan string) <-chan string {
	canalMultiplexado := make(chan string)

	go func() {
		for {
			select {
			case msg1 := <-canal1:
				canalMultiplexado <- msg1
			case msg2 := <-canal2:
				canalMultiplexado <- msg2
			}
		}
	}()

	return canalMultiplexado
}