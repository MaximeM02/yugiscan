import typing
from alphaz.models.database.operators import Operators
from models.databases.yugiscan import Edition, Card
from core import core
from models.dataclass.edition import EditionModel

DB = core.db

def select_editions(
    show_cards: bool = False,
    code_edition: str = None,
    code_card: str = None,
    name_edition: str = None,
    name_card: str = None,
    page: int = None,
    page_size: int = None,
) -> typing.List[EditionModel]:

    return DB.select(
        model=Edition,
        #dataclass=EditionModel,
        optional_filters=[
            {
                Edition.e_code: {Operators.LIKE: code_edition},
                Edition.e_name: {Operators.LIKE: name_edition},
                Edition.cards: {
                    Operators.HAS: {
                        Card.c_real_code: {Operators.LIKE:code_card},
                        Card.c_name: {Operators.LIKE:name_card}
                    }
                }
            }
        ],
        disabled_relationships=[Edition.cards.key] if not show_cards else  None,
        order_by=[Edition.e_release_date.key, f"{Edition.cards.key}.{Card.c_index_in_edition.key}"],
        order_by_direction=['ASC', 'DESC'],
        page=None if page is None else page,
        per_page=None if page_size is None else page_size,
    )