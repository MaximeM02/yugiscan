import typing
from alphaz.models.database.operators import Operators
from models.databases.yugiscan import Card
from core import core

DB = core.db

def select_cards(
    card_id: typing.List[int] = None,
    edition_id: str = None,
    code: str = None,
    name: str = None,
    rarity: typing.List[str] = None,
    attribute: typing.List[str] = None,
    type: typing.List[str] = None,
    level: typing.List[str] = None,
    attack: typing.List[str] = None,
    defense: typing.List[str] = None,
    link_classification: typing.List[str] = None,
    text: str = None,
    img_url: str = None,
    quantity_in_edition: typing.List[int] = None,
    real_code: str = None,
    index_in_edition: typing.List[int] = None,
    template_id: typing.List[int] = None,
    page: int = None,
    page_size: int = None,
) -> typing.List[Card]:

    return DB.select(
        model=Card,
        optional_filters=[
            {Card.c_id: {Operators.IN: card_id}},
            {Card.c_edition_id: {Operators.IN: edition_id}},
            {Card.c_code: {Operators.LIKE: code}},
            {Card.c_name: {Operators.LIKE: name}},
            {Card.c_rarity: {Operators.IN: rarity}},
            {Card.c_attribute: {Operators.IN: attribute}},
            {Card.c_type: {Operators.IN: type}},
            {Card.c_level: {Operators.IN: level}},
            {Card.c_attack: {Operators.IN: attack}},
            {Card.c_defense: {Operators.IN: defense}},
            {Card.c_link_classification: {Operators.IN: link_classification}},
            {Card.c_text: {Operators.LIKE: text}},
            {Card.c_img_url: {Operators.LIKE: img_url}},
            {Card.c_quantity_in_edition: {Operators.IN: quantity_in_edition}},
            {Card.c_real_code: {Operators.LIKE: real_code}},
            {Card.c_index_in_edition: {Operators.IN: index_in_edition}},
            {Card.c_template_id: {Operators.IN: template_id}},
        ],
        page=page,
        per_page=page_size,
    )

def select_card_by_id(
    card_id: int
) -> Card:

    return next(
        iter(
            select_cards(card_id=card_id)
        ), None
    )