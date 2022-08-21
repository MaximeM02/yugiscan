from alphaz.utils.api import route, api, Parameter
from models.dataclass.user_collection import UserCollectionDataclass
from utils.yugiscan import user_collection_function

from core import core

from typing import List, Union, Optional

API = core.api
LOG = core.get_logger('api')


@route(
    path='yugiscan/user_collection',
    description='',
    logged=True,
    methods=['GET'],
    parameters=[
        Parameter('code_edition', ptype=str),
        Parameter('code_card', ptype=str),
        Parameter('name_edition', ptype=str),
        Parameter('name_edition', ptype=str),
        Parameter('page', ptype=int, required=True),
        Parameter('page_size', ptype=int, required=True, default=15),
    ],
)
def get_edition_collection() -> Optional[dict]:
    return user_collection_function.select_user_collection(**api.get_parameters(), user_id=api.get_logged_user()["id"])


@route(
    path='yugiscan/user_collection',
    description='',
    logged=True,
    methods=['POST'],
    parameters=[
        Parameter('card_id', ptype=int, required=True),
        Parameter('nbr_copy', ptype=int, required=True, default=1),
    ],
)
def insert_user_collection() -> Optional[UserCollectionDataclass]:
    return user_collection_function.insert_user_collection(**api.get_parameters(), user_id=api.get_logged_user()["id"])

@route(
    path='yugiscan/user_collection',
    description='',
    logged=True,
    methods=['DELETE'],
    parameters=[
        Parameter('card_id', ptype=int, required=True),
    ],
)
def delete_user_collection() -> bool:
    return user_collection_function.delete_user_collection(**api.get_parameters(), user_id=api.get_logged_user()["id"])

