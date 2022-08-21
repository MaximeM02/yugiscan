from alphaz.utils.api import route, api, Parameter
from utils.yugiscan import card_function

from core import core, API

from typing import List, Union, Optional

LOG = core.get_logger('api')

@route(
    path='yugiscan/cards',
    description='',
    methods=['GET'],
    parameters=[
        Parameter('card_id', ptype=List[int]),
        Parameter('edition_id', ptype=List[int]),
        Parameter('code', ptype=str),
        Parameter('name', ptype=str),
        Parameter('rarity', ptype=List[str]),
        Parameter('attribute', ptype=List[str]),
        Parameter('type', ptype=List[str]),
        Parameter('level', ptype=List[str]),
        Parameter('attack', ptype=List[str]),
        Parameter('defense', ptype=List[str]),
        Parameter('link_classification', ptype=List[str]),
        Parameter('text', ptype=str),
        Parameter('img_url', ptype=str),
        Parameter('quantity_in_edition', ptype=List[int]),
        Parameter('real_code', ptype=str),
        Parameter('index_in_edition', ptype=List[int]),
        Parameter('template_id', ptype=List[int]),
        Parameter('page', ptype=int, required=True),
        Parameter('page_size', ptype=int, required=True, default=15)
    ],
)
def get_cards() -> Optional[Union[List[card_function.Card], dict]]:
    return card_function.select_cards(**api.get_parameters())

