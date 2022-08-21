from alphaz.utils.api import route, api, Parameter
from utils.yugiscan import user_function as u

from core import core, API

from typing import List, Union, Optional, Dict

API = core.api
LOG = core.get_logger('api')

@route(
    path='yugiscan/user',
    description='',
    methods=['GET'],
    parameters=[],
)
def get_user() -> Dict[str, str]:
    return u.select_user(**api.get_parameters())

