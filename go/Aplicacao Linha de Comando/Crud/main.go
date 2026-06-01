package main

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

//go mod init crud
//go get github.com/gorilla/mux
//go get modernc.org/sqlite

func main() {

	router := mux.NewRouter()
	log.Printf("Servidor rodando na porta 8000")

	log.Fatal(http.ListenAndServe(":8000", router))
	// router.HandleFunc("/produtos", GetProdutos).Methods("GET")
	// router.HandleFunc("/produtos/{id}", GetProduto).Methods("GET")
	// router.HandleFunc("/produtos", CreateProduto).Methods("POST")
	// router.HandleFunc("/produtos/{id}", UpdateProduto).Methods("PUT")
	// router.HandleFunc("/produtos/{id}", DeleteProduto).Methods("DELETE")

}
