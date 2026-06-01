package main

import (
	"database/sql"
	"log"

	_ "modernc.org/sqlite"
)

//go mod init banco-de-dados
//go get github.com/go-sql-driver/mysql
//sqlite
//go get github.com/mattn/go-sqlite3
//go get modernc.org/sqlite

type User struct {
	ID    int
	Name  string
	Email string
}

func main() {
	//Conecatão com o sqlite
	db, err := sql.Open("sqlite", "banco.db")

	if err != nil {
		log.Fatal(err)
	}
	log.Printf("Conexão com o banco de dados estabelecida com sucesso!")
	defer db.Close()
	if err := db.Ping(); err != nil {
		log.Fatal(err)
	}
	log.Printf("Conexão com o banco de dados verificada com sucesso!")

	//Criando uma tabela
	createTableSQL := `CREATE TABLE IF NOT EXISTS users (
		"id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
		"name" TEXT,
		"email" TEXT
	);`
	_, err = db.Exec(createTableSQL)
	if err != nil {
		log.Fatal(err)
	}
	log.Printf("Tabela 'users' criada com sucesso!")
	//Inserindo dados
	insertUserSQL := `INSERT INTO users (name, email) VALUES (?, ?)`
	_, err = db.Exec(insertUserSQL, "Nick  Silva", "joao.silva@example.com")
	if err != nil {
		log.Fatal(err)
	}
	log.Printf("Usuário 'João Silva' inserido com sucesso!")
	//Consultando dados
	rows, err := db.Query("SELECT id, name, email FROM users")
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()

	Usuarios := make([]User, 0)
	// for rows.Next() {
	// 	var id int
	// 	var name, email string
	// 	if err := rows.Scan(&id, &name, &email); err != nil {
	// 		log.Fatal(err)
	// 	}
	// 	Usuarios = append(Usuarios, User{ID: id, Name: name, Email: email})
	// }
	log.Printf("Lista de usuários:")
	for _, user := range Usuarios {
		log.Printf("- ID: %d, Nome: %s, Email: %s", user.ID, user.Name, user.Email)
	}
}
