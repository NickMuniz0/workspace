package banco

import (
	"database/sql"
	"log"

	_ "modernc.org/sqlite"
)

//go get modernc.org/sqlite

func Conectar() (*sql.DB, error) {
	log.Println("Conectando ao banco de dados...")

	db, err := sql.Open("sqlite", "banco.db")

	if err != nil {
		log.Fatal(err)
	}
	log.Printf("Conexão com o banco de dados estabelecida com sucesso!")
	defer db.Close()
	if err := db.Ping(); err != nil {
		log.Fatal(err)
	}
	return db, err
}
