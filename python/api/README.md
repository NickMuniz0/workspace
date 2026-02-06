Projeto: CRUD simples com FastAPI e SQLite

Instruções rápidas (Windows):

1. Criar e ativar um ambiente virtual:

```powershell
python -m venv .venv
.venv\Scripts\Activate.ps1   # PowerShell
# ou
.venv\Scripts\activate.bat    # cmd
```

2. Instalar dependências:

```powershell
pip install -r requirements.txt
```

3. Rodar a aplicação:

```powershell
uvicorn main:app --reload
```

A API expõe endpoints:
- `POST /items/` - criar item (json: `name`, `description`)
- `GET /items/` - listar itens
- `GET /items/{id}` - obter item
- `PUT /items/{id}` - atualizar item
- `DELETE /items/{id}` - apagar item

O banco SQLite `database.db` será criado automaticamente na pasta do projeto.