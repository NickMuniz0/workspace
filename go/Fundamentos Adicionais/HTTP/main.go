package main

import (
	"fmt"
	"html/template"
	"log"
	"net/http"
)

var templates = template.Must(template.ParseGlob("*.html"))

type User struct {
	Name  string
	Email string
}

func home(w http.ResponseWriter, r *http.Request) {

	u := User{Name: "João", Email: "joao@example.com"}

	templates.ExecuteTemplate(w, "home.html", u)
}

func main() {
	// O HTTP é um protocolo de comunicação utilizado para transferir dados entre um cliente e um servidor na web. Ele é a base da comunicação na internet, permitindo que os navegadores acessem páginas web, enviem formulários, e interajam com servidores.
	// O HTTP é um protocolo sem estado, o que significa que cada requisição é independente e não mantém informações sobre as requisições anteriores. Ele utiliza métodos como GET, POST, PUT, DELETE, entre outros, para indicar a ação desejada em relação aos recursos do servidor.
	// O HTTP é amplamente utilizado para a construção de APIs (Application Programming Interfaces) e para a comunicação entre clientes e servidores na web. Ele é fundamental para a troca de informações e a interação entre diferentes sistemas na internet.
	fmt.Println("Servidor rodando na porta 5000...")
	http.HandleFunc("/home", home)
	log.Fatal(http.ListenAndServe(":5000", nil))
}
