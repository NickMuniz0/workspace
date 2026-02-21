package main

func main(){
	canal := make(chan string,2) //buffer de 2 posições
	canal <- "Olá Mundo"
	canal <- "Programando em go"
	//canal <- "Terceiro valor"  //se ativar vai dar deadlock  porque temos um buffer de 2 posições e estamos tentando enviar um terceiro valor sem ler os anteriores

	mensagem := <-canal
	mensagem2 := <-canal

	println(mensagem)
	println(mensagem2)

}