from alphaz.utils.api import route, api, Parameter
from utils.yugiscan import edition_function

from core import core, API

from typing import List, Union, Optional

LOG = core.get_logger('api')

@route(
    path='yugiscan/editions',
    description='',
    methods=['GET'],
    parameters=[
        Parameter('show_cards', ptype=bool, default=False),
        Parameter('code_edition', ptype=str),
        Parameter('code_card', ptype=str),
        Parameter('name_edition', ptype=str),
        Parameter('name_edition', ptype=str),
        Parameter('page', ptype=int, required=True),
        Parameter('page_size', ptype=int, required=True, default=15),
    ],
)
def get_editions() -> Optional[Union[List[edition_function.EditionModel], dict]]:
    return edition_function.select_editions(**api.get_parameters())

