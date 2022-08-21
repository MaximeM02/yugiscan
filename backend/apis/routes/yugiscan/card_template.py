from alphaz.utils.api import route, api, Parameter
from utils.yugiscan import card_template_function

from core import core, API

from typing import List, Union, Optional


LOG = core.get_logger('api')

@route(
    path='yugiscan/card_template',
    description='',
    methods=['GET'],
    parameters=[],
)
def get_card_template() -> Optional[List[card_template_function.CardTemplateModel]]:
    return card_template_function.select_card_template(**api.get_parameters())

