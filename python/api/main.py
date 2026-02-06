from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import sqlite3
from typing import List, Optional
from fastapi.responses import JSONResponse
from contextlib import asynccontextmanager
import os
import uvicorn

DB = "database.db"


def get_conn():
    conn = sqlite3.connect(DB)
    conn.row_factory = sqlite3.Row
    return conn


def init_db():
    conn = get_conn()
    conn.execute(
        """
    CREATE TABLE IF NOT EXISTS items (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        description TEXT
    )
    """
    )
    conn.commit()
    conn.close()


class ItemIn(BaseModel):
    name: str
    description: Optional[str] = None


class Item(ItemIn):
    id: int


@asynccontextmanager
async def lifespan(app: FastAPI):
    init_db()
    yield


app = FastAPI(title="CRUD API com SQLite", lifespan=lifespan)


@app.post("/items/", response_model=Item, status_code=201)
def create_item(item: ItemIn):
    conn = get_conn()
    cur = conn.cursor()
    cur.execute("INSERT INTO items (name, description) VALUES (?, ?)", (item.name, item.description))
    conn.commit()
    item_id = cur.lastrowid
    conn.close()
    return {"id": item_id, **item.dict()}


@app.get("/items/", response_model=List[Item])
def list_items():
    conn = get_conn()
    cur = conn.cursor()
    rows = cur.execute("SELECT id, name, description FROM items").fetchall()
    conn.close()
    return [Item(id=row["id"], name=row["name"], description=row["description"]) for row in rows]


@app.get("/items/{item_id}", response_model=Item)
def get_item(item_id: int):
    conn = get_conn()
    row = conn.execute("SELECT id, name, description FROM items WHERE id = ?", (item_id,)).fetchone()
    conn.close()
    if row is None:
        raise HTTPException(status_code=404, detail="Item not found")
    return Item(id=row["id"], name=row["name"], description=row["description"])


@app.put("/items/{item_id}", response_model=Item)
def update_item(item_id: int, item: ItemIn):
    conn = get_conn()
    cur = conn.cursor()
    cur.execute("UPDATE items SET name = ?, description = ? WHERE id = ?", (item.name, item.description, item_id))
    if cur.rowcount == 0:
        conn.close()
        raise HTTPException(status_code=404, detail="Item not found")
    conn.commit()
    row = conn.execute("SELECT id, name, description FROM items WHERE id = ?", (item_id,)).fetchone()
    conn.close()
    return Item(id=row["id"], name=row["name"], description=row["description"])


@app.delete("/items/{item_id}", status_code=204)
def delete_item(item_id: int):
    conn = get_conn()
    cur = conn.cursor()
    cur.execute("DELETE FROM items WHERE id = ?", (item_id,))
    if cur.rowcount == 0:
        conn.close()
        raise HTTPException(status_code=404, detail="Item not found")
    conn.commit()
    conn.close()
    return JSONResponse(status_code=204, content=None)


if __name__ == "__main__":
    port = int(os.getenv("PORT", "8000"))
    uvicorn.run(app, host="0.0.0.0", port=port)
