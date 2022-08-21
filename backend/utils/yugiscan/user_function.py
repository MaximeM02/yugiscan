from models.databases.yugiscan import ListUser
from core import core

DB = core.db

def select_user():
    return DB.select(
                model=ListUser
            )